/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.ProblemDetail;

import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminProblemsView extends Composite implements AdminProblemsPresenter.AdminProblemsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    @Override
    public Column<ProblemDetail, String> getTextColumn() {
        return textColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<ProblemDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<ProblemDetail> getSelectionModel() {
        return selectionModel;
    }

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminProblemsView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<ProblemDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<ProblemDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<ProblemDetail, String> textColumn;
    private Column<ProblemDetail, String> priceColumn;
    private Column<ProblemDetail, String> stateColumn;
    private Column<ProblemDetail, Date> dateColumn;

    @Override
    public void createView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        changesLabel.setText("0");
    }

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
//        dataGrid = new DataGrid<ProblemDetail>(KEY_PROVIDER);
        dataGrid = new DataGrid<ProblemDetail>();
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

//        selectionModel = new SingleSelectionModel<ProblemDetail>(KEY_PROVIDER);
        selectionModel = new SingleSelectionModel<ProblemDetail>();
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<ProblemDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        addColumn(new EditTextCell(), "ID", 50, new GetValue<String>() {

            @Override
            public String getValue(ProblemDetail object) {
                return Long.toString(object.getId());
            }
        });
        // DemandName
        textColumn = addColumn(new EditTextCell(), "Text", 100, new GetValue<String>() {

            @Override
            public String getValue(ProblemDetail object) {
                return object.getText();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(ProblemDetail problemDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<ProblemDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<ProblemDetail, C> column = new Column<ProblemDetail, C>(cell) {

            @Override
            public C getValue(ProblemDetail object) {
                return getter.getValue(object);
            }
        };
        column.setSortable(true);
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
//    /**
//     * The key provider that provides the unique ID of a ProblemDetail.
//     */
//    private static final ProvidesKey<ProblemDetail> KEY_PROVIDER = new ProvidesKey<ProblemDetail>() {
//
//        @Override
//        public Object getKey(ProblemDetail item) {
//            return item == null ? null : item.getDemandId();
//        }
//    };

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }
}