package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.RatingDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;
import java.util.List;

public class SupplierRatingsView extends Composite
        implements SupplierRatingsPresenter.SupplierRatingsLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierRatingsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierRatingsLayoutViewUiBinder.class);

    interface SupplierRatingsLayoutViewUiBinder extends UiBinder<Widget, SupplierRatingsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField RatingDetailView ratingDetail;
    @UiField SimplePanel detailPanel;
    @UiField HorizontalPanel toolBar;
    @UiField Label tableNameLabel;
    /** Class attributes. **/
    private static final List<String> GRID_COLUMNS = Arrays.asList(new String[]{"title"});

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initTable() {
        pager = new UniversalPagerWidget();
        dataGrid = new UniversalAsyncGrid<DemandRatingsDetail>(DemandRatingsDetail.KEY_PROVIDER, GRID_COLUMNS);
        dataGrid.setSelectionModel(new SingleSelectionModel<DemandRatingsDetail>());
        dataGrid.setPageSize(pager.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

        pager.setDisplay(dataGrid);

        initTableColumns();
    }

    private void initTableColumns() {
        // Demand title
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnDemandTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    return ((DemandRatingsDetail) object).getDemandTitle();
                }
            });
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalAsyncGrid getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    @Override
    public RatingDetailView getRatingDetail() {
        return ratingDetail;
    }

    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    //Widget view
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}