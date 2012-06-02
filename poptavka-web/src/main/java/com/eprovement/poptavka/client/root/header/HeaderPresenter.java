package com.eprovement.poptavka.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.main.login.LoginPopupPresenter;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;

@Presenter(view = HeaderView.class)
public class HeaderPresenter extends BasePresenter<IHeaderView, RootEventBus>
        implements IHeaderPresenter {

    // TODO praso - Nemali by sme mat LocalizablaMessages ako static instanciu v Storage?
    // Preco ju vytvarame znovu na tomto mieste?
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private boolean loggedIn = false;
    private LoginPopupPresenter login;

    public void onStart() {
        GWT.log("Header presenter loaded");
        eventBus.setHeader(view);
    }

    @Override
    public void bind() {
        view.getLoginLink().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Logged in? " + loggedIn);
                //neda nahradid tym cookies? ... co lepsie pouzivat tie cookies ci premennu?
                if (loggedIn) {
                    eventBus.atHome();
                    eventBus.goToHomeWelcomeModule(null);
                } else {
                    onInitLoginWindow();
                }
            }
        });
        Window.addWindowClosingHandler(new Window.ClosingHandler() {

            @Override
            public void onWindowClosing(ClosingEvent event) {
                if (eventBus.getHistory().getToken().contains("atHome")) {
                    eventBus.getHistory().forward();
                }
                if (eventBus.getHistory().getToken().contains("atAccount")) {
                    eventBus.getHistory().back();
                }
                Cookies.setCookie("login", "no");
            }
        });
    }

    public void onInitLoginWindow() {
        login = eventBus.addHandler(LoginPopupPresenter.class);
        login.onLogin();
    }

    public void onAtHome() {
        this.loggedIn = false;
        view.getLoginLink().setText(MSGS.logIn());
    }

    public void onAtAccount() {
        this.loggedIn = true;
        view.getLoginLink().setText(MSGS.logOut());
    }
}