package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.eprovement.poptavka.client.common.LoadingPopupPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.address.AddressSelectorPresenter;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter;
import com.eprovement.poptavka.client.common.login.LoginPopupPresenter;
import com.eprovement.poptavka.client.common.services.ServicesSelectorPresenter;
import com.eprovement.poptavka.client.root.activation.ActivationCodePopupPresenter;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter;
import com.eprovement.poptavka.client.root.interfaces.IRootView;
import com.eprovement.poptavka.client.root.interfaces.IRootView.IRootPresenter;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.BusinessRole;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import java.util.List;

@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, RootEventBus>
        implements IRootPresenter {

    private CategorySelectorPresenter categorySelector = null;
    private LocalitySelectorPresenter localitySelector = null;
    private AddressSelectorPresenter addressSelector = null;
    private LoginPopupPresenter login = null;
    private LoadingPopupPresenter loading = null;
    private ActivationCodePopupPresenter activation = null;
    private ServicesSelectorPresenter services = null;

    /**************************************************************************/
    /* Layout events.                                                         */
    /**************************************************************************/
    public void onSetHeader(IsWidget header) {
        GWT.log("Header widget set");
        view.setHeader(header);
    }

    public void onSetMenu(IsWidget menu) {
        GWT.log("Menu widget set");
        view.setMenu(menu);
    }

    public void onSetSearchBar(IsWidget searchBar) {
        GWT.log("Search bar widget set");
        view.setSearchBar(searchBar);
    }

    public void onSetUpSearchBar(IsWidget searchView) {
        GWT.log("Search bar widget set up");
        view.setUpSearchBar(searchView);
    }

    public void onSetBody(IsWidget body) {
        GWT.log("Body widget set");
        view.setBody(body);
    }

    public void onSetFooter(IsWidget footer) {
        GWT.log("Footer widget set");
        view.setFooter(footer);
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * When your application starts, you may want to automatically fire an event
     * so that actions needed at first can occur.
     */
    public void onStart() {
        GWT.log("Root presenter loaded");
        eventBus.atHome();
        eventBus.goToSearchModule();
        //If application is started because of URL, the token is full when starting
        if (History.getToken().isEmpty()) {
            GWT.log("++++++++++++++++++++++++++++NORMAL START OF APP");
            // normal start of app
            Storage.setAppCalledByURL(false);
            eventBus.goToHomeWelcomeModule(null);
        } else {
            // start of app by entering URL token
            GWT.log("++++++++++++++++++++++++++++START BY URL OF APP");
            Storage.setAppCalledByURL(true);
            // populate Storage by UserDetail and BusinessUserDetail

        }
    }

    /**
     * This event will be called in case an error occurs while loading the
     * ChildModule code.
     *
     * @param reason - An object may be fired for the event used in case of
     * error but the type of this object must be compatible with
     * java.lang.Throwable. In this case, the error returned by the
     * RunAsync object is passed to the event.
     */
    public void onErrorOnLoad(Throwable reason) {
        eventBus.displayError(500, null); // HTTP 500 - internal server error.
    }

    /**
     * This event will be called before starting to load the ChildModule code.
     * You can for example decide to display a wait popup.
     */
    public void onBeforeLoad() {
        onLoadingShow(Storage.MSGS.loading());
    }

    /**
     * This event will be called after the code is done loading.
     * You can for example decide to hide a wait popup.
     */
    public void onAfterLoad() {
        onLoadingHide();
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onAtAccount() {
        GWT.log("User has logged in and his user data are about to be retrieved");
//        showDevelUserInfoPopupThatShouldBedeletedAfter();
        // notify all components that user has logged in
        view.setLogoStyle(Storage.RSCS.layout().userLogo());
    }

    public void onAtHome() {
        // notify all components that user has logged out
        view.setLogoStyle(Storage.RSCS.layout().homeLogo());
    }

    /**************************************************************************/
    /* Login events                                                           */
    /**************************************************************************/
    /**
     * Method displays the LoginPoupView so that user can enter credentials and log in.
     */
    public void onLogin(int widgetToLoad) {
        if (login != null) {
            eventBus.removeHandler(login);
        }
        login = eventBus.addHandler(LoginPopupPresenter.class);
        login.loadWidget(widgetToLoad);
    }

    /**
     * Method displays the LoginPoupView and enter credentials to auto log in.
     */
    public void onAutoLogin(String email, String password, int widgetToLoad) {
        onLogin(widgetToLoad);
        login.doAutoLogin(email, password);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onNotFound() {
        eventBus.start();
        view.setBody(new Label("Page not found"));
    }

    public void onInitDemandAdvForm(SimplePanel holderWidget) {
    }

    public void onLoadingShow(String loadingMessage) {
        if (loading == null) {
            loading = eventBus.addHandler(LoadingPopupPresenter.class);
        }
        loading.show(loadingMessage);
    }

    public void onLoadingShowWithAnchor(String loadingMessage, Widget anchor) {
        if (loading == null) {
            loading = eventBus.addHandler(LoadingPopupPresenter.class);
        }
        loading.show(loadingMessage, anchor);
    }

    public void onLoadingHide() {
        if (loading != null) {
            loading.hide();
        }
    }

    /** multiple presenters handling methods **/
    public void onInitCategoryWidget(SimplePanel holderPanel, int checkboxes, int displayCountsOfWhat,
            List<CategoryDetail> categoriesToSet) {
        if (categorySelector != null) {
            eventBus.removeHandler(categorySelector);
        }
        categorySelector = eventBus.addHandler(CategorySelectorPresenter.class);
        categorySelector.initCategoryWidget(holderPanel, checkboxes, displayCountsOfWhat, categoriesToSet);
    }

    public void onInitLocalityWidget(SimplePanel holderPanel, int checkboxes, int displayCountsOfWhat,
            List<LocalityDetail> localitiesToSet) {
        if (localitySelector != null) {
            eventBus.removeHandler(localitySelector);
        }
        localitySelector = eventBus.addHandler(LocalitySelectorPresenter.class);
        localitySelector.initLocalityWidget(holderPanel, checkboxes, displayCountsOfWhat, localitiesToSet);
    }

    public void onInitAddressWidget(SimplePanel holderPanel) {
        if (addressSelector != null) {
            eventBus.removeHandler(addressSelector);
        }
        addressSelector = eventBus.addHandler(AddressSelectorPresenter.class);
        addressSelector.initAddressWidget(holderPanel);
    }

    public void onInitDemandBasicForm(SimplePanel holderWidget) {
    }

    public void onInitEmailDialogPopup() {
        eventBus.addHandler(EmailDialogPopupPresenter.class);
    }

    public void onInitActivationCodePopup(BusinessUserDetail client, int widgetToLoad) {
        if (activation != null) {
            eventBus.removeHandler(activation);
        }
        activation = eventBus.addHandler(ActivationCodePopupPresenter.class);
        activation.initActivationCodePopup(client, widgetToLoad);
    }

    public void onInitServicesWidget(SimplePanel holderWidget) {
        if (services != null) {
            eventBus.removeHandler(services);
        }
        services = eventBus.addHandler(ServicesSelectorPresenter.class);
        services.initServicesWidget(holderWidget);
    }
//    private static final int OFFSET_X = 60;
//    private static final int OFFSET_Y = 35;

//    private void createLoadingPopup(String loadingMessage, Widget anchor) {
//        popup = new PopupPanel(false, true);
//        popup.setGlassEnabled(true);
//        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
//        popup.setWidget(new LoadingPopup(loadingMessage));
//        int top = anchor.getAbsoluteTop() + (anchor.getOffsetHeight() / 2);
//        int left = anchor.getAbsoluteLeft() + (anchor.getOffsetWidth() / 2)
//                - OFFSET_X;
//        popup.showRelativeTo(anchor);
//        GWT.log("AbsoluteLeft: " + anchor.getAbsoluteLeft() + " OffsetWidth: "
//                + (anchor.getOffsetWidth()));
//        GWT.log("AbsoluteTop: " + anchor.getAbsoluteTop() + " Offsetheight: "
//                + (anchor.getOffsetHeight()));
//
//        GWT.log("L: " + left + " T: " + top);
//
//        popup.show();
//    }
//
//    private void createLoadingPopup(String loadingMessage) {
//        popup = new PopupPanel(false, true);
//        popup.setGlassEnabled(true);
//        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
//        popup.setWidget(new LoadingPopup(loadingMessage));
//        popup.setPopupPosition((Window.getClientWidth() / 2) - OFFSET_X,
//                (Window.getClientHeight() / 2) - OFFSET_Y);
//        popup.show();
//    }
    // TODO delete for production
    private void showDevelUserInfoPopupThatShouldBedeletedAfter() {
        final DialogBox userInfoPanel = new DialogBox(false, false);
        userInfoPanel.setText("User Info Box");
        userInfoPanel.setWidth("200px");
        String br = "<br />";
        StringBuilder sb = new StringBuilder("<b>User Info:</b>" + br);
        UserDetail userDetail = Storage.getUser();
        BusinessUserDetail user = Storage.getBusinessUserDetail();
        sb.append("ID: " + user.getUserId() + br);

        sb.append("<i>-- Business user roles --</i>" + br);
        if (user.getBusinessRoles().contains(BusinessRole.CLIENT)) {
            sb.append("<b><i>BusinessRole: CLIENT</i></b>" + br);
            sb.append("ClientID: " + user.getClientId() + br);
            sb.append("Demands Messages: " + "n/a" + " / " + "n/a" + br);
            sb.append("Demands Offers: " + "n/a" + " / " + "n/a" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getBusinessRoles().contains(BusinessRole.SUPPLIER)) {
            sb.append("<b><i>BusinessRole: SUPPLIER</i></b>" + br);
            sb.append("SupplierID: " + user.getSupplierId() + br);
            sb.append("Potentional Demands: " + "n/a" + " / " + "n/a" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getBusinessRoles().contains(BusinessRole.PARTNER)) {
            sb.append("<b><i>BusinessRole: PARTNER</i></b>" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
//        if (user.getBusinessRoles().contains(BusinessRole.OPERATOR)) {
//            sb.append("<b><i>OPERATOR</i></b>" + br);
//            sb.append("<i>-- -- -- --</i>" + br);
//        }
        sb.append("<i>-- User access roles --</i>" + br);
        if (userDetail.getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
            sb.append("<b><i>ADMIN</i></b>" + br);
        }
        if (userDetail.getAccessRoles().contains(CommonAccessRoles.CLIENT)) {
            sb.append("<b><i>CLIENT</i></b>" + br);
        }
        if (userDetail.getAccessRoles().contains(CommonAccessRoles.SUPPLIER)) {
            sb.append("<b><i>SUPPLIER</i></b>" + br);
        }
        sb.append("<i>-- -- -- --</i>" + br);
        sb.append("Messages: " + "n/a" + " / " + "n/a" + br);

        HTML content = new HTML(sb.toString());
        Button closeButton = new Button("Close");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                userInfoPanel.hide();
            }
        });
        FlowPanel m = new FlowPanel();
        m.add(content);
        m.add(closeButton);
        userInfoPanel.add(m);
        userInfoPanel.setPopupPosition(Window.getClientWidth() - 200, 20);
        userInfoPanel.show();
    }

    /**
     * Request RootEventBus to create DetailWrapperPresenter.
     */
    public void onRequestDetailWrapperPresenter() {
        DetailsWrapperPresenter detailSection = eventBus.addHandler(DetailsWrapperPresenter.class);
        eventBus.responseDetailWrapperPresenter(detailSection);
    }
}
