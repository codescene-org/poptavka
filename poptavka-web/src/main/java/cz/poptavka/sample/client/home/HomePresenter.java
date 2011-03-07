package cz.poptavka.sample.client.home;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomePresenter.class.getName());

    public enum AnchorEnum {
        FIRST, SECOND, THIRD
    }

    public interface HomeInterface extends LazyView {

        HasClickHandlers getCreateDemandBtn();

        void setContent(AnchorEnum anchor, Widget content);

        Widget getWidgetView();

        HasClickHandlers getDisplayDemandsBtn();
    }

    public void bindView() {
        view.getCreateDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initDemandCreation();
            }
        });
    }

    public void bindDisplay() {
        view.getCreateDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.displayDemands();
            }
        });
    }

    public void onInitHome() {
        LOGGER.info("on init home");
        eventBus.setBodyHolderWidget(view.getWidgetView());

        //locality selector testing
        //eventBus.initLocalitySelector(AnchorEnum.FIRST);

        //demand creation
//        LOGGER.info("demand creation called...");
//        eventBus.initDemandCreation();

    }

    /**
     * Set content widget to selected part of page
     *
     * @param anchor place where to place widget
     * @param body widget to be placed
     */
    public void onSetAnchorWidget(AnchorEnum anchor, Widget content) {
        view.setContent(anchor, content);
    }

}
