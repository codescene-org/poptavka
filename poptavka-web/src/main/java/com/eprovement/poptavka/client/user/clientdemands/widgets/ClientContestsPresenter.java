/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = ClientContestsView.class)
public class ClientContestsPresenter
        extends LazyPresenter<ClientContestsPresenter.ClientContestsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientContestsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<ClientProjectDetail> getDemandGrid();

        UniversalTableWidget getContestGrid();

        //Buttons
        Button getBackBtn();

        Button getAcceptBtn();

        Button getDenyBtn();

        Button getReplyBtn();

        //ListBox
        ListBox getActions();

        //Other
        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        //Setter
        void setContestTableVisible(boolean visible);

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
    private long lastOpenedProjectContest = -1;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Selection Handlers
        addDemandTableSelectionHandler();
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        // Buttons Actions
        addBackButtonHandler();
        addActionChangeHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientContests(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_PROJECTS);
        searchDataHolder = filter;
        view.getDemandGrid().getDataCount(eventBus, searchDataHolder);

        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        if (this.detailSection == null) {
            eventBus.requestDetailWrapperPresenter();
        }
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
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
        detailSection.develRemoveReplyWidget();
        eventBus.removeHandler(detailSection);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientOfferedProjects(List<ClientProjectDetail> data) {
        GWT.log("++ onResponseClientsOfferedProjects");

        view.getDemandGrid().updateRowData(data);
    }

    public void onDisplayClientProjectContestants(List<ClientProjectContestantDetail> data) {
        GWT.log("++ onResponseClientsProjectContestants");

        //TODO - ked vymeneny detail objekt v UniversalTableWIdgete tak odkomentovat
//        view.getContestGrid().getGrid().updateRowData(data);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(ClientProjectContestantDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestContest(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
        detailSection.requestConversation(124L, 289L, 149L);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        //TODO odkomentovat ak vymeneny detail
//        view.getContestGrid().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {
//
//            @Override
//            public void update(Boolean value) {
//                List<ClientProjectContestantDetail> rows = view.getContestGrid().getVisibleItems();
//                for (ClientProjectContestantDetail row : rows) {
//                    ((MultiSelectionModel) view.getContestGrid().getSelectionModel()).setSelected(row, value);
//                }
//            }
//        });
    }

    public void addStarColumnFieldUpdater() {
        //TODO odkomentovat ak vymeneny detail
//        view.getContestGrid().getStarColumn().setFieldUpdater(
//        new FieldUpdater<ClientProjectContestantDetail, Boolean>() {
//
//            @Override
//            public void update(int index, ClientProjectContestantDetail object, Boolean value) {
////              TableDisplay obj = (TableDisplay) object;
//                object.setStarred(!value);
//                view.getContestGrid().redraw();
//                Long[] item = new Long[]{object.getUserMessageId()};
//                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
//            }
//        });
    }

    public void addTextColumnFieldUpdaters() {
        FieldUpdater textFieldUpdater = new FieldUpdater<ClientProjectContestantDetail, String>() {

            @Override
            public void update(int index, ClientProjectContestantDetail object, String value) {
                if (lastOpenedProjectContest != object.getUserMessageId()) {
                    lastOpenedProjectContest = object.getUserMessageId();
                    object.setRead(true);
                    view.getContestGrid().getGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getContestGrid().getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getContestGrid().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getContestGrid().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getContestGrid().getDeliveryColumn().setFieldUpdater(textFieldUpdater);
        view.getContestGrid().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Buttons
    private void addBackButtonHandler() {
        view.getBackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getDemandGrid().getSelectionModel().setSelected(
                        (ClientProjectDetail) ((SingleSelectionModel)
                        view.getDemandGrid().getSelectionModel()).getSelectedObject(), false);
                view.setContestTableVisible(false);
            }
        });
    }

    private void addActionChangeHandler() {
        view.getActions().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActions().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getContestGrid().getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getContestGrid().getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getContestGrid().getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getContestGrid().getSelectedIdList(), false);
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
                Storage.setCurrentlyLoadedView(Constants.CLIENT_PROJECT_CONTESTANTS);
                ClientProjectDetail selected = (ClientProjectDetail) ((SingleSelectionModel)
                        view.getDemandGrid().getSelectionModel()).getSelectedObject();
                if (selected != null) {
                    selected.setRead(true);
                    Storage.setDemandId(selected.getDemandId());
                    view.setDemandTitleLabel(selected.getDemandTitle());
                    view.setContestTableVisible(true);
                    view.getContestGrid().getGrid().getDataCount(eventBus, null);
                }
            }
        });
    }
}