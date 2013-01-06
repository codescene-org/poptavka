/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = SupplierDemandsView.class, multiple = true)
public class SupplierDemandsPresenter extends LazyPresenter<
        SupplierDemandsPresenter.SupplierDemandsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalTableWidget getTableWidget();

        SimplePanel getDetailPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.POTENTIAL;
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedPotentialDemand = -1;
    private long selectedSupplierDemandId = -1;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Range Change Handlers
        tableRangeChangeHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addReplyColumnFieldUpdater();
        addSendOfferColumnFieldUpdater();
        addColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);

        eventBus.setUpSearchBar(new Label("Supplier's projects attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTableWidget().getGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        eventBus.requestDetailWrapperPresenter();
    }

    public void onInitSupplierDemandsByHistory(int tablePage, long selectedId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectSupplierDemandsMenu(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getTableWidget().getPager().getPage() != tablePage) {
            //cancel range change event in asynch data provider
            view.getTableWidget().getGrid().cancelRangeChangedEvent();
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getTableWidget().getPager().setPage(tablePage);

        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTableWidget()
                .getGrid().getSelectionModel();
        for (SupplierPotentialDemandDetail detail : (Set<
                SupplierPotentialDemandDetail>) selectionModel.getSelectedSet()) {
            if (detail.getDemandId() == selectedId) {
                wasEqual = true;
            }
        }
        this.selectedSupplierDemandId = selectedId;
        if (selectedId == -1) {
            selectionModel.clear();
        } else {
            if (!wasEqual) {
                lastOpenedPotentialDemand = -1;
                eventBus.getSupplierDemand(selectedId);
            }
        }

        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            view.getTableWidget().getGrid().getDataCount(eventBus, new SearchDefinition(
                    tablePage * view.getTableWidget().getGrid().getPageSize(),
                    view.getTableWidget().getGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getTableWidget().getGrid(), view.getDetailPanel(), type);
            this.detailSection.getView().getReplyHolder().getOfferReplyBtn().setVisible(true);
            this.detailSection.setTabVisibility(DetailsWrapperPresenter.SUPPLIER, false);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierDemands(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getTableWidget().getGrid().getDataProvider().updateRowData(
                view.getTableWidget().getGrid().getStart(), data);

        if (selectedSupplierDemandId != -1) {
            eventBus.getSupplierDemand(lastOpenedPotentialDemand);
        }
    }

    public void onSelectSupplierDemand(SupplierPotentialDemandDetail detail) {
        eventBus.setHistoryStoredForNextOne(false);
        textFieldUpdater.update(-1, detail, null);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(SupplierPotentialDemandDetail detail) {
        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestConversation(detail.getThreadRootId(), Storage.getUser().getUserId());
    }

    public void onSendMessageResponse(MessageDetail sentMessage, ViewType handlingType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(handlingType)) {
//            detailSection.addConversationMessage(sentMessage);
        }
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void tableRangeChangeHandler() {
        view.getTableWidget().getGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory(
                        view.getTableWidget().getPager().getPage(), selectedSupplierDemandId);
            }
        });
    }
    // TableWidget handlers

    public void addCheckHeaderUpdater() {
        view.getTableWidget().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getTableWidget().getGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getTableWidget().getGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getTableWidget().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                    @Override
                    public void update(int index, IUniversalDetail object, Boolean value) {
                        object.setStarred(!value);
                        view.getTableWidget().getGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addReplyColumnFieldUpdater() {
        view.getTableWidget().getReplyImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addSendOfferColumnFieldUpdater() {
        view.getTableWidget().getSendOfferImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addOfferReply();
                    }
                });
    }

    public void addColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<SupplierPotentialDemandDetail, Object>() {
            @Override
            public void update(int index, SupplierPotentialDemandDetail object, Object value) {
                //getUserMessageDetail() -> getOfferDetail() due to fake data
//                if (lastOpenedPotentialDemand != object.getDemandId()) {
//                    lastOpenedPotentialDemand = object.getDemandId();
                object.setRead(true);
//                    view.getTableWidget().getGrid().redraw();
                displayDetailContent(object);
                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTableWidget()
                        .getGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
                eventBus.createTokenForHistory(
                        view.getTableWidget().getPager().getPage(),
                        object.getDemandId());
//                }
            }
        };
        view.getTableWidget().getClientNameColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getPriceColumn().setFieldUpdater(textFieldUpdater);
//        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getUrgencyColumn().setFieldUpdater(textFieldUpdater);
//        view.getTableWidget().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionChangeHandler() {
        view.getTableWidget().getActionBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getTableWidget().getActionBox().getSelectedIndex()) {
                    case Constants.READ:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case Constants.UNREAD:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
                        break;
                    case Constants.STARED:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case Constants.UNSTARED:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}