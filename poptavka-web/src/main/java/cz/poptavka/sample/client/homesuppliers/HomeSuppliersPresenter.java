package cz.poptavka.sample.client.homesuppliers;

import com.google.gwt.event.dom.client.ChangeEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.resources.StyleResource;

import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Presenter(view = HomeSuppliersView.class)
public class HomeSuppliersPresenter
        extends BasePresenter<HomeSuppliersPresenter.SuppliersViewInterface, HomeSuppliersEventBus> {

    public interface SuppliersViewInterface { //extends LazyView {
        //******** ROOT SECTION **********

        HorizontalPanel getRootSection();

        SingleSelectionModel getSelectionRootModel();

        void displayRootCategories(int columns, ArrayList<CategoryDetail> categories);

        //******** CHILD SECTION **********
        HTMLPanel getChildSection();

        Label getFilterLabel();

        int getPageSize();

        ListBox getPageSizeCombo();

        Button getContactBtn();

        FlowPanel getPath();

        void addPath(Widget widget);

        void removePath(); //removes last one

        DataGrid getDataGrid();

        SimplePager getPager();

        Widget getWidgetView();

        SingleSelectionModel getSelectionCategoryModel();

        SingleSelectionModel getSelectionSupplierModel();

        SplitLayoutPanel getSplitter();

        void displaySubCategories(int columns, ArrayList<CategoryDetail> categories);

        void displaySuppliersDetail(FullSupplierDetail userDetail);

        void hideSuppliersDetail();

    }
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    //differ if category was selected from menu, or from path
    private Boolean wasSelection = false;
    //for asynch data retrieving
    private AsyncDataProvider dataProvider = null;
    private int start = 0;
    private int length = 0;
    //for asynch data sorting
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "businessUser.businessUserData.companyName", "overalRating", "", ""
    };
    private List<String> gridColumns = Arrays.asList(columnNames);
    //others
    //columns number of root chategories in parent widget
    private static final int COLUMNS = 4;
    private String location = null; //home, user

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
    /* Navigation events                                                      */
    /**************************************************************************/
    private SearchModuleDataHolder searchDataHolder = null;

    public void onGoToHomeSuppliersModule(SearchModuleDataHolder searchDataHolder, String location) {
        eventBus.loadingShow(MSGS.loading());
        this.location = location;
        this.searchDataHolder = searchDataHolder;

        //ROOT section
        if (searchDataHolder == null) {

            view.getChildSection().setVisible(false);
            view.getRootSection().setVisible(true);
            //display root categories on whole page
            view.getFilterLabel().setVisible(false);
            // add root to path
            eventBus.addToPath(new CategoryDetail(0L, ""), location);
            //get Root Categories
            eventBus.getCategories();
            //CHILD section
        } else {
            view.getFilterLabel().setVisible(true);
            view.getChildSection().setVisible(true);
            view.getRootSection().setVisible(false);
            // create path
            eventBus.getCategoryParents(searchDataHolder.getHomeSuppliers().getSupplierCategory().getId(), location);
            // get Sub Categories
            eventBus.getSubCategories(searchDataHolder.getHomeSuppliers().getSupplierCategory().getId());
        }

        //If module not loaded, do it.
        if (!Storage.getCurrentlyLoadedModule().equals("homeSuppliers")) {
            Storage.setCurrentlyLoadedModule("homeSuppliers");
            Storage.setCurrentlyLoadedView("homeSuppliers");
        }
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onCreateAsyncDataProvider(final int totalFound) {
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<SupplierDetail>() {

            @Override
            protected void onRangeChanged(HasData<SupplierDetail> display) {
                display.setRowCount(totalFound);
                if (totalFound == 0) {
                    return;
                }
                start = display.getVisibleRange().getStart();
                length = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.ASC);
                eventBus.getSuppliers(start, start + length, searchDataHolder, orderColumns);

                eventBus.loadingHide();
            }

        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        this.createAsyncSortHandler();
    }

    public void createAsyncSortHandler() {
        AsyncHandler sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<FullSupplierDetail, String> column = (Column<FullSupplierDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getSuppliers(start, start + length, searchDataHolder, orderColumns);
            }

        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    @Override
    public void bind() {
        view.getSelectionRootModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionRootModel().getSelectedObject();

                if (selected != null) {
                    wasSelection = true;
                    eventBus.displayChildWidget(selected.getId());
                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                        searchDataHolder.initHomeSuppliers();
                    }
                    searchDataHolder.getHomeSuppliers().setSupplierCategory(
                            new CategoryDetail(selected.getId(), selected.getName()));
                    eventBus.addToPath(selected, location);
                }
            }

        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.loadingShow(MSGS.loading());
                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    wasSelection = true;
                    view.hideSuppliersDetail();
                    view.getSelectionSupplierModel().setSelected(
                            view.getSelectionSupplierModel().getSelectedObject(), false);

                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                        searchDataHolder.initHomeSuppliers();
                    }
                    searchDataHolder.getHomeSuppliers().setSupplierCategory(
                            new CategoryDetail(selected.getId(), selected.getName()));
                    eventBus.getSubCategories(selected.getId());
                    eventBus.addToPath(selected, location);
                }
            }

        });
        view.getSelectionSupplierModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullSupplierDetail selected = (FullSupplierDetail) view.getSelectionSupplierModel().getSelectedObject();

                if (selected != null) {
                    view.displaySuppliersDetail(selected);
                }
            }

        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getDataGrid().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSizeCombo().
                        getItemText(view.getPageSizeCombo().getSelectedIndex()));

                view.getDataGrid().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);
            }

        });

    }

    //
    //                      **** DISPLAY ****
    //
    /* ROOT CATEGORIES */
    public void onDisplayRootcategories(ArrayList<CategoryDetail> rootCategories) {
        view.displayRootCategories(COLUMNS, rootCategories);
        eventBus.loadingHide();
    }

    /**
     * Cares for displaying sub categories of chosen parent category and displaying suppliers for
     * chosen selection of sub categories and parent category.
     * @param subcategories - subcategories of selected parentCategory to be displayed
     * @param parentCategory - parent category of returned subcategories. Need for retrieving data from DB.
     */
    /* SUB CATEGORIES */
    public void onDisplaySubCategories(ArrayList<CategoryDetail> subcategories, Long parentCategory) {
        view.displaySubCategories(COLUMNS, subcategories);

        if (!wasSelection) { // ak nebola vybrana kategoria zo zoznamu, ale klik na hyperlink na vyvolanie historie
            searchDataHolder.getHomeSuppliers().setSupplierCategory(new CategoryDetail(parentCategory, ""));
        }
        eventBus.getSuppliersCount(searchDataHolder);
        wasSelection = false;
        eventBus.loadingHide();
    }

    /**
     * Display suppliers of selected category.
     * @param list
     */
    /* SUPPLIERS */
    public void onDisplaySuppliers(List<FullSupplierDetail> list) {
        view.getDataGrid().redraw();
        view.getDataGrid().setRowData(start, list);
    }

    /**
     * Called from suppliers display widget root by click on one of root category.
     * Methods initialize Path, fills category list of Suppliers widget.
     * @param category selected from root categories in parent widget
     */
    /* CHILD WIDGET */
    public void onDisplayChildWidget(Long id) {
//        eventBus.loadingShow(MSGS.loading());

        view.getChildSection().setVisible(true);
        view.getRootSection().setVisible(false);

        eventBus.getSubCategories(id);
    }

    //
    //                  **** PATH ****
    //
    /**
     * Manages path and history calls. Adds and removes items from path according to history.
     * @param categoryDetail - category from url to be loaded
     */
    public void onUpdatePath(ArrayList<CategoryDetail> categories, String location) {
        view.getPath().clear();

        //Root
        Hyperlink rootLink = new Hyperlink("root",
                getTokenGenerator().addToPath(new CategoryDetail(0L, ""), location));
        rootLink.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
        view.addPath(rootLink);

        //Anything else
        for (int i = categories.size() - 1; i > 0; i--) {
            String token = getTokenGenerator().addToPath(categories.get(i), location);
            Hyperlink subLink = new Hyperlink(" -> " + categories.get(i).getName(), token);
            subLink.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
            view.addPath(subLink);
        }

        //Last
        // for last user addToPath method to generate token
        eventBus.addToPath(categories.get(0), location);
    }

    /**
     * Adds hyperlink to path.
     * @param categoryDetail
     */
    public void onAddToPath(CategoryDetail categoryDetail, String location) {
//        pointer++;
        String symbol = "";
        //Root
        if (categoryDetail.getId() == 0) {
            view.getPath().clear();
            symbol = "root";
        } else {
            symbol = " -> ";
        }
        //Anything else
        String token = getTokenGenerator().addToPath(categoryDetail, location);
        Hyperlink link = new Hyperlink(symbol + categoryDetail.getName(), token);

        link.setStylePrimaryName(StyleResource.INSTANCE.common().hyperlinkInline());
        view.addPath(link);
    }

}