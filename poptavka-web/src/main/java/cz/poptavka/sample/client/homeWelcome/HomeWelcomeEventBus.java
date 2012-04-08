/*
 * HomeWelcomEventBus is very first home page to be loaded.
 *
 * Specification:
 * Wireframe: http://www.webgres.cz/axure/
 */
package cz.poptavka.sample.client.homeWelcome;

import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@Events(startView = HomeWelcomeView.class, module = HomeWelcomeModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface HomeWelcomeEventBus extends EventBus {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = HomeWelcomePresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = HomeWelcomePresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module. This module is not asynchronous.
     *
     * @param filter - defines data holder to be displayed in advanced search bar
     */
    @Event(handlers = HomeWelcomePresenter.class, historyConverter = HomeWelcomeHistoryConverter.class)
    String goToHomeWelcomeModule(SearchModuleDataHolder filter);

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Presenters.                                 */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by Handlers.                                   */
    /**************************************************************************/
}