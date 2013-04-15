/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategoryTreeViewModel;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.footer.FooterView;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.cell.CreatedDateCell;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.celltree.CustomCellTree;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Home demands module's view.
 *
 * @author praso, Martin Slavkovsky
 */
public class HomeDemandsView extends OverflowComposite implements ReverseViewInterface<HomeDemandsPresenter>,
        HomeDemandsPresenter.HomeDemandsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeDemandsViewUiBinder uiBinder = GWT.create(HomeDemandsViewUiBinder.class);

    interface HomeDemandsViewUiBinder extends UiBinder<Widget, HomeDemandsView> {
    }

    /**************************************************************************/
    /* Home Supplier presenter                                                */
    /**************************************************************************/
    private HomeDemandsPresenter homeDemandsPresenter;

    @Override
    public void setPresenter(HomeDemandsPresenter presenter) {
        this.homeDemandsPresenter = presenter;
    }

    @Override
    public HomeDemandsPresenter getPresenter() {
        return homeDemandsPresenter;
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid<FullDemandDetail> dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField(provided = true) CellTree cellTree;
    // Others
    @UiField(provided = true) Widget footer;
    @UiField DockLayoutPanel detailPanel;
    @UiField Label bannerLabel, filterLabel;
    @UiField DemandDetailView demandDetail;
    /** Class attributes. **/
    private List<String> gridColumns = Arrays.asList(new String[]{"createdDate", "title", "locality", "endDate"});
    // Using category detail key provider in selection model, allow us to have
    // displayed alwas only one node. The other are automaticaly closed.
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);
    private @Inject FooterView footerView;
    /** Constants. **/
    private static final String LOCALITY_COL_WIDTH = "150px";

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        footer = footerView;

        initTableAndPager();
        initCellTree();
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    public void initCellTree() {
        // Workaround for issue: CellTree disappeared when clicking but outside
        // tree nodes
        CellTree.Resources resource = GWT.create(CustomCellTree.class);
        cellTree = new CellTree(new CategoryTreeViewModel(selectionCategoryModel,
                homeDemandsPresenter.getCategoryService(), homeDemandsPresenter.getEventBus(),
                Constants.WITHOUT_CHECK_BOXES, CategoryCell.DISPLAY_COUNT_OF_DEMANDS,
                homeDemandsPresenter.getCategoryLoadingHandler()), null, resource);
        cellTree.setAnimationEnabled(true);
    }

    /**
     * Initialize this example.
     */
    private void initTableAndPager() {
        pager = new UniversalPagerWidget();
        // Create a CellTable
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalAsyncGrid<FullDemandDetail>(gridColumns, pager.getPageSize(), resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        // Selection handler
        dataGrid.setSelectionModel(new SingleSelectionModel<FullDemandDetail>());

        // bind pager to grid
        pager.setDisplay(dataGrid);

        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {

        // Date of creation
        /**********************************************************************/
        dataGrid.addColumn(new CreatedDateCell(), Storage.MSGS.columnCreatedDate(), true, Constants.COL_WIDTH_DATE,
            new UniversalAsyncGrid.GetValue<Date>() {
                @Override
                public Date getValue(Object object) {
                    return ((FullDemandDetail) object).getCreated();
                }
            }
        );

        // Demand Info
        /***********************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnDemandTitle(), true, Constants.COL_WIDTH_TITLE,
            new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    return ((FullDemandDetail) object).getTitle();
                }
            }
        );

        // Locality
        /**********************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnLocality(), false, LOCALITY_COL_WIDTH,
            new UniversalAsyncGrid.GetValue<String>() {
                @Override
                public String getValue(Object object) {
                    StringBuilder str = new StringBuilder();
                    for (LocalityDetail loc : ((FullDemandDetail) object).getLocalities()) {
                        str.append(loc.getName());
                        str.append(",\n");
                    }
                    if (!str.toString().isEmpty()) {
                        str.delete(str.length() - 2, str.length());
                    }
                    return str.toString();
                }
            }
        );

        // Urgence
        /**********************************************************************/
        dataGrid.addUrgentColumn();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** CellTree. **/
    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    @Override
    public SingleSelectionModel<CategoryDetail> getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

    /** Table. **/
    @Override
    public UniversalAsyncGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    /** Filter. **/
    @Override
    public Label getFilterLabel() {
        return filterLabel;
    }

    /** Other. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void displayDemandDetail(FullDemandDetail fullDemandDetail) {
        bannerLabel.setVisible(false);
        detailPanel.setVisible(true);

        demandDetail.setDemanDetail(fullDemandDetail);
    }

    @Override
    public void hideDemandDetail() {
        bannerLabel.setVisible(true);
        detailPanel.setVisible(false);
    }
}
