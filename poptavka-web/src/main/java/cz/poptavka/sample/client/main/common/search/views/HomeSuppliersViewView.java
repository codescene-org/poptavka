package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;

public class HomeSuppliersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeSuppliersViewView> {
    }
    @UiField
    TextBox companyName, ratingFrom, ratingTo, supplierDescription;
    @UiField
    ListBox category, locality;
    @UiField
    Button clearBtn;

    public HomeSuppliersViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        if (!companyName.getText().equals("")) {
            data.getFilters().add(new FilterItem("name", FilterItem.OPERATION_FROM, companyName.getText()));
        }
        if (!supplierDescription.getText().equals("")) {
            data.getFilters().add(new FilterItem("description", FilterItem.OPERATION_FROM,
                    supplierDescription.getText()));
        }
        if (!ratingFrom.getText().equals("0")) {
            data.getFilters().add(new FilterItem("overalRating", FilterItem.OPERATION_FROM, ratingFrom.getText()));
        }
        if (!ratingTo.getText().equals("100")) {
            data.getFilters().add(new FilterItem("overalRating", FilterItem.OPERATION_FROM, ratingTo.getText()));
        }
        return data;
    }

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateRatingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        companyName.setText("");
        supplierDescription.setText("");
        category.setSelectedIndex(0);
        locality.setSelectedIndex(0);
        ratingFrom.setText("0");
        ratingTo.setText("100");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}