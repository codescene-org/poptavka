/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Presenter(view = ClientOffersView.class)
public class ClientOffersPresenter
        extends LazyPresenter<ClientOffersPresenter.ClientOffersLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientOffersLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientDemandDetail> getDemandGrid();

        UniversalTableWidget getOfferGrid();

        //Pager
        SimplePager getDemandPager();

        SimplePager getOfferPager();

        //Buttons
        Button getBackBtn();

        //Other
        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        //Setter
        void setDemandTableVisible(boolean visible);

        void setOfferTableVisible(boolean visible);

        void setDemandTitleLabel(String text);
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.EDITABLE;
    private DetailsWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemandOffer = -1;
    private long selectedClientOfferedDemandId = -1;
    private long selectedClientOfferedDemandOfferId = -1;
    private FieldUpdater textFieldUpdater = null;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Range Change Handlers
        demandGridRangeChangeHandler();
        offerGridRangeChangeHandler();
        // Selection Handlers
        addDemandTableSelectionHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addReplyColumnFieldUpdater();
        addAcceptOfferColumnFieldUpdater();
        addDeclineOfferColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Buttons Actions
        addBackButtonHandler();
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        eventBus.setUpSearchBar(new Label("Client's contests attibure's selector will be here."));
        searchDataHolder = filter;
        eventBus.createTokenForHistory1(0);
        view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
    }

    public void onInitClientOfferedDemandsByHistory(int parentTablePage, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_OFFERED_DEMANDS);
        //
        //If current page differ to stored one, cancel events that would be fire automatically but with no need
        if (view.getDemandPager().getPage() != parentTablePage) {
            view.getDemandGrid().cancelRangeChangedEvent(); //cancel range change event in asynch data provider
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getDemandPager().setPage(parentTablePage);
        //Change visibility
        view.setOfferTableVisible(false);
        view.setDemandTableVisible(true);

        this.selectedClientOfferedDemandId = -1;

        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            view.getDemandGrid().getDataCount(eventBus, new SearchDefinition(
                    parentTablePage * view.getDemandGrid().getPageSize(),
                    view.getDemandGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
    }

    public void onInitClientOfferedDemandOffersByHistory(ClientDemandDetail clientDemandDetail,
            int childTablePage, long childId, SearchModuleDataHolder filterHolder) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
        //Select Menu - my demands - selected
        eventBus.selectClientDemandsMenu(Constants.CLIENT_DEMANDS);
        //
        clientDemandDetail.setRead(true);
        Storage.setDemandId(clientDemandDetail.getDemandId());
        view.setDemandTitleLabel(clientDemandDetail.getDemandTitle());
        view.setDemandTableVisible(false);
        view.setOfferTableVisible(true);
        //
        if (view.getOfferGrid().getPager().getPage() != childTablePage) {
            view.getOfferGrid().getGrid().cancelRangeChangedEvent(); //cancel range change event in asynch data provider
            eventBus.setHistoryStoredForNextOne(false);
        }
        view.getOfferGrid().getPager().setPage(childTablePage);
        //if selection differs to the restoring one
        boolean wasEqual = false;
        MultiSelectionModel selectionModel = (MultiSelectionModel) view.getOfferGrid().getGrid().getSelectionModel();
        for (ClientDemandConversationDetail cdcd : (Set<
                ClientDemandConversationDetail>) selectionModel.getSelectedSet()) {
            if (cdcd.getSupplierId() == childId) {
                wasEqual = true;
            }
        }
        if (wasEqual) {
            this.selectedClientOfferedDemandOfferId = childId;
        } else {
            selectionModel.clear();
            eventBus.getClientDemandConversation(childId);
        }

        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            view.getOfferGrid().getGrid().getDataCount(eventBus, new SearchDefinition(
                    childTablePage * view.getOfferGrid().getGrid().getPageSize(),
                    view.getOfferGrid().getGrid().getPageSize(),
                    filterHolder,
                    null));
        }

        eventBus.displayView(view.getWidgetView());
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(view.getWrapperPanel(), type);
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientOfferedDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemands");

        view.getDemandGrid().getDataProvider().updateRowData(
                view.getDemandGrid().getStart(), data);

        if (selectedClientOfferedDemandId != -1) {
            eventBus.getClientOfferedDemand(selectedClientOfferedDemandId);
        }
    }

    public void onDisplayClientOfferedDemandOffers(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        view.getOfferGrid().getGrid().getDataProvider().updateRowData(
                view.getOfferGrid().getGrid().getStart(), data);

        if (selectedClientOfferedDemandOfferId != -1) {
            eventBus.getClientOfferedDemand(selectedClientOfferedDemandOfferId);
        }
    }

    public void onSelectClientOfferedDemand(ClientDemandDetail detail) {
        view.getDemandGrid().getSelectionModel().setSelected(detail, true);
    }

    public void onSelectClientOfferedDemandOffer(FullOfferDetail detail) {
        eventBus.setHistoryStoredForNextOne(false); //don't create token
        //nestaci oznacit v modeli, pretoze ten je viazany na checkboxy a akcie, musim
        //nejak vytvorit event na upadatefieldoch
        //Dolezite je len detail, ostatne atributy sa nepouzivaju
        textFieldUpdater.update(-1, detail, null);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(FullOfferDetail detail) {
        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestConversation(detail.getMessageId(),
                detail.getUserMessageId(), Storage.getUser().getUserId());
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getOfferGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<IUniversalDetail> rows = view.getOfferGrid().getGrid().getVisibleItems();
                for (IUniversalDetail row : rows) {
                    ((MultiSelectionModel) view.getOfferGrid().getGrid()
                            .getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getOfferGrid().getStarColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, Boolean>() {
                    @Override
                    public void update(int index, IUniversalDetail object, Boolean value) {
                        object.setStarred(!value);
                        view.getOfferGrid().getGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addReplyColumnFieldUpdater() {
        view.getOfferGrid().getReplyImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        detailSection.getView().getReplyHolder().addQuestionReply();
                    }
                });
    }

    public void addAcceptOfferColumnFieldUpdater() {
        view.getOfferGrid().getAcceptOfferImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        eventBus.requestAcceptOffer(object.getUserMessageId()); //good attribute ???
                    }
                });
    }

    public void addDeclineOfferColumnFieldUpdater() {
        view.getOfferGrid().getDeclineOfferImageColumn().setFieldUpdater(
                new FieldUpdater<IUniversalDetail, ImageResource>() {
                    @Override
                    public void update(int index, IUniversalDetail object, ImageResource value) {
                        eventBus.requestDeclineOffer(object.getUserMessageId()); //good attribute ???
                    }
                });
    }

    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                if (lastOpenedDemandOffer != object.getUserMessageId()) {
                    lastOpenedDemandOffer = object.getUserMessageId();
                    object.setRead(true);
                    view.getOfferGrid().getGrid().redraw();
                    view.setDemandTableVisible(false);
                    view.setOfferTableVisible(true);
                    displayDetailContent(object);
                    MultiSelectionModel selectionModel = (MultiSelectionModel) view.getOfferGrid().getGrid()
                            .getSelectionModel();
                    selectionModel.clear();
                    selectionModel.setSelected(object, true);
                    eventBus.createTokenForHistory2(Storage.getDemandId(),
                            view.getOfferPager().getPage(), object.getOfferDetail().getId());
                }
            }
        };
        view.getOfferGrid().getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getEndDateColumn().setFieldUpdater(textFieldUpdater);
        view.getOfferGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setOfferTableVisible(false);
            }
        });
    }

    private void addActionChangeHandler() {
        view.getOfferGrid().getActionBox().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getOfferGrid().getActionBox().getSelectedIndex()) {
                    case Constants.READ:
                        eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedIdList(), true);
                        break;
                    case Constants.UNREAD:
                        eventBus.requestReadStatusUpdate(view.getOfferGrid().getSelectedIdList(), false);
                        break;
                    case Constants.STARED:
                        eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedIdList(), true);
                        break;
                    case Constants.UNSTARED:
                        eventBus.requestStarStatusUpdate(view.getOfferGrid().getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //SelectionHandlers
    private void addDemandTableSelectionHandler() {
        view.getDemandGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMAND_OFFERS);
                ClientDemandDetail selected = (ClientDemandDetail) ((SingleSelectionModel) view.getDemandGrid()
                        .getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selected.setRead(true);
                    Storage.setDemandId(selected.getDemandId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.setOfferTableVisible(true);
                    view.getOfferGrid().getGrid().getDataCount(eventBus, null);
                    eventBus.createTokenForHistory2(selected.getDemandId(), view.getOfferPager().getPage(), -1);
                }
            }
        });
    }

    /**************************************************************************/
    /**
     * If demand table range change (page changed), create token for new data (different page).
     */
    private void demandGridRangeChangeHandler() {
        view.getDemandGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory1(view.getDemandPager().getPage());
            }
        });
    }

    /**
     * If offer table range change (page changed), create token for new data (different page).
     */
    private void offerGridRangeChangeHandler() {
        view.getOfferGrid().getGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                eventBus.createTokenForHistory2(
                        Storage.getDemandId(), view.getOfferPager().getPage(), -1);
            }
        });
    }
}