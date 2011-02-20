package cz.poptavka.sample.client.main;

import java.util.ArrayList;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.domain.address.Locality;

@Events(startView = MainView.class, historyOnStart = true)
public interface MainEventBus extends EventBus {

    /**
     * Init method for layout and history initialization. Layout initialization
     * contains menu injection.
     * To inject more widgets, update onStart() method of MainPresenter
     */
    @InitHistory
    @Event(handlers = { MainPresenter.class, MainHandler.class })
    void start();

    @Event(handlers = MainPresenter.class)
    void setData(ArrayList<Locality> data);

//    @Event(handlers = MainPresenter.class)
//    void setHolderWidget(boolean bodyContainer, Widget content);

}
