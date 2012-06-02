/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.eprovement.poptavka.shared.domain.type.DemandStatusType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminOurPaymentDetailsView.class)
public class AdminOurPaymentDetailsPresenter
        extends LazyPresenter<AdminOurPaymentDetailsPresenter.AdminOurPaymentDetailsInterface, AdminEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("AdminDemandsPresenter");
    private Map<Long, FullDemandDetail> dataToUpdate = new HashMap<Long, FullDemandDetail>();
    private Map<Long, FullDemandDetail> originalData = new HashMap<Long, FullDemandDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminOurPaymentDetailsInterface extends LazyView {

        Widget getWidgetView();

        DataGrid<FullDemandDetail> getDataGrid();

        Column<FullDemandDetail, String> getDemandTitleColumn();

        Column<FullDemandDetail, String> getDemandTypeColumn();

        Column<FullDemandDetail, String> getDemandStatusColumn();

        Column<FullDemandDetail, Date> getDemandExpirationColumn();

        Column<FullDemandDetail, Date> getDemandEndColumn();

        SingleSelectionModel<FullDemandDetail> getSelectionModel();

        SimplePanel getAdminDemandDetail();

        SimplePager getPager();

        int getPageSize();

        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        ListBox getPageSizeCombo();
    }
    private AsyncDataProvider dataProvider = null;
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "id", "client", "title", "type", "status", "validTo", "endDate"
    };
    private int start = 0;
    private List<String> gridColumns = Arrays.asList(columnNames);
    private SearchModuleDataHolder searchDataHolder; //need to remember for asynchDataProvider if asking for more data

    public void onCreateAdminOurPaymentDetailAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[5], OrderType.DESC);
        dataProvider = new AsyncDataProvider<FullDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullDemandDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminOurPaymentDetails(start, start + length, searchDataHolder, orderColumns);
                Storage.hideLoading();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }

    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;

                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<FullDemandDetail, String> column = (Column<FullDemandDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getAdminOurPaymentDetails(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onInitOurPaymentDetails(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_OUR_PAYMENT_DETAILS);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
//        eventBus.getAdminDemandsOurPaymentDetailsCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    public void onShowAdminTabOurPaymentDetails(List<PaymentDetail> paymentDetailList) {
        dataProvider.updateRowData(start, paymentDetailList);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    @Override
    public void bindView() {
        setDemandTitleColumnUpdater();
        setDemandTypeColumnUpdater();
        setDemandStatusColumnUpdater();
        setDemandExpirationColumnUpdater();
        setDemandEndColumnUpdater();
        addSelectionChangeHandler();
        addPageChangeHandler();
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    dataProvider.updateRowCount(0, true);
                    dataProvider = null;
                    view.getDataGrid().flush();
                    view.getDataGrid().redraw();
                    eventBus.getAdminDemandsCount(searchDataHolder);
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (FullDemandDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeDemand(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
    }

    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
//                        eventBus.updateOurPaymentDetail(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    private void addPageChangeHandler() {
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
    }

    private void addSelectionChangeHandler() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getDemandId())) {
                    eventBus.showAdminDemandDetail(dataToUpdate.get(
                            view.getSelectionModel().getSelectedObject().getDemandId()));
                } else {
                    eventBus.showAdminDemandDetail(view.getSelectionModel().getSelectedObject());
                }
                eventBus.setDetailDisplayedDemand(true);
            }
        });
    }

    private void setDemandEndColumnUpdater() {
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getEndDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setEndDate(value);
//                    eventBus.addOurPaymentDetailToCommit(object, "other");
                }
            }
        });
    }

    private void setDemandExpirationColumnUpdater() {
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getValidToDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setValidToDate(value);
//                    eventBus.addOurPaymentDetailToCommit(Commit(object, "other");
                }
            }
        });
    }

    private void setDemandStatusColumnUpdater() {
        view.getDemandStatusColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (DemandStatusType demandStatusType : DemandStatusType.values()) {
                    if (demandStatusType.getValue().equals(value)) {
                        if (!object.getDemandStatus().equals(demandStatusType.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandStatus(demandStatusType.name());
//                            eventBus.addOurPaymentDetailToCommit(object, "demand");
                        }
                    }
                }
            }
        });
    }

    private void setDemandTypeColumnUpdater() {
        view.getDemandTypeColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (ClientDemandType clientDemandType : ClientDemandType.values()) {
                    if (clientDemandType.getValue().equals(value)) {
                        if (!object.getDemandType().equals(clientDemandType.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandType(clientDemandType.name());
//                            eventBus.addOurPaymentDetailToCommit(object, "demand");
                        }
                    }
                }
            }
        });
    }

    private void setDemandTitleColumnUpdater() {
        view.getDemandTitleColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                if (!object.getTitle().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setTitle(value);
//                    eventBus.addOurPaymentDetailToCommit(object, "demand");
                }
            }
        });
    }

    private Boolean detailDisplayed = false;

    public void onAddOurPaymentDetailToCommit(PaymentDetail data) {
        //TODO Martin - otestovat, alebo celkom zrusit cistocne auktualizovanie
//        if (metadataToUpdate.containsKey(data.getDemandId())) {
//            dataToUpdate.remove(data.getDemandId());
//            metadataToUpdate.remove(data.getDemandId());
//            metadataToUpdate.put(data.getDemandId(), "all");
//        } else {
//            dataToUpdate.put(data.getDemandId(), data);
//            metadataToUpdate.put(data.getDemandId(), dataType);
//        }
//        if (detailDisplayed) {
//            eventBus.showAdminDemandDetail(data);
//        }
//        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
//        view.getDataGrid().flush();
//        view.getDataGrid().redraw();
    }

    public void onSetDetailDisplayedDemand(Boolean displayed) {
        detailDisplayed = displayed;
    }
}