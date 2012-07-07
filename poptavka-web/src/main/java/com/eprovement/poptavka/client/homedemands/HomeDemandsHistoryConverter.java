package com.eprovement.poptavka.client.homedemands;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;

/**
 * History converter class. Handles history for HomeDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeDemands")
public class HomeDemandsHistoryConverter implements HistoryConverter<HomeDemandsEventBus> {

    /**
     * To convert token for goToHomeDemandsModule method.
     *
     * @param searchDataHolder - Provided by search module. Holds data to filter.
     * @return token string like module/method?param, where param = home / user
     */
    public String onGoToHomeDemandsModule(SearchModuleDataHolder filter) {
        /* Martin - Nemusi to byt, pretoze v convertFromToken to neberiem vobec do uvahy.
        Ale pre testovacie ucely ale vhodne. Potom moze odstranit */
        if (Storage.getUser() == null) {
            return "location=home";
        } else {
            return "location=user";
        }
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates goToHomeDemandsModule method in HomeDemandsHistoryConverter class.
     * @param eventBus - HomeDemandsEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeDemandsEventBus eventBus) {
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_DEMANDS_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_DEMANDS_MODULE);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
