package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.user.client.ui.PopupPanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.common.search.SearchModuleEventBus;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

@Presenter(view = AdminPaymentMethodsViewView.class)
public class AdminPaymentMethodsViewPresenter
        extends BasePresenter<SearchModulePresenter.SearchModulesViewInterface, SearchModuleEventBus> {

    @Override
    public void bind() {
    }

    public void onInitAdminPaymentMethodsView(PopupPanel popupPanel) {
        popupPanel.setWidget(view.getWidgetView());
    }
}