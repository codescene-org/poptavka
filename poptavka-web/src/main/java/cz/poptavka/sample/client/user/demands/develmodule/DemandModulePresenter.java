package cz.poptavka.sample.client.user.demands.develmodule;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;

/**
 * For every user - default tab
 *
 * Consists of left menu only and holder for demands related
 * stuff. Contains list of all demands for faster working with
 * demands.
 *
 * @author Beho
 */

@Presenter(view = DemandModuleView.class, multiple = true)
public class DemandModulePresenter
        extends
        BasePresenter<DemandModulePresenter.DemandsLayoutInterface, DemandModuleEventBus> {

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        //beho devel section
        Anchor getDevelAnchor();

        /**
         * this is the old section, maybe restore
        //menu client
        void setMyDemandsToken(String link);

        void setOffersToken(String link);

        void setNewDemandToken(String link);

        void setAllDemandsToken(String linkString);

        void setAllSuppliersToken(String linkString);

        //menu supplier
        void setPotentialDemandsToken(String link);
         */

        SimplePanel getContentPanel();
    }

    public void bind() {
        /**
        // MENU - CLIENT
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());
        view.setAllDemandsToken(getTokenGenerator().invokeAtDemands());
        view.setAllSuppliersToken(getTokenGenerator().invokeAtSuppliers());

        //MENU - SUPPLIER
        view.setPotentialDemandsToken(getTokenGenerator().invokePotentialDemands());
        */
        //DEVEl - BEHO
        view.getDevelAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    //TODO
    //later add UserDetail as parameter
    public void onInitDemandModule() {
        // hiding window for this is after succesfull Userhandler call
        Storage.get().showLoading(Storage.MSGS.progressDemandsLayoutInit());
//        if (user.getRoleList().contains(Role.CLIENT)) {
            // TODO execute client specific demands init methods/calls
//        }
//        if (user.getRoleList().contains(Role.SUPPLIER)) {
            // TODO using businessUserId and NOT supplier ID
            // DEBUGING popup
            // TODO Maybe do nothing
//            PopupPanel panel = new PopupPanel(true);
//            panel.getElement().setInnerHTML("<br/>Getting SupplierDemands<")
//            panel.center();
//            eventBus.getPotentialDemands(user.getId());
//        }

        Window.alert("DemandModule Initialized");
//        eventBus.setTabWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onDisplayContent(Widget contentWidget) {
        Storage.toggleLoading(view.getContentPanel());
        view.setContent(contentWidget);
    }



}