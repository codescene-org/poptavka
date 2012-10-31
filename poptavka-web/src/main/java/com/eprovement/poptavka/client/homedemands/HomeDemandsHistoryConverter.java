package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homesuppliers.TreeItem;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import java.util.LinkedList;

/**
 * History converter class. Handles history for HomeDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "demands")
public class HomeDemandsHistoryConverter implements HistoryConverter<HomeDemandsEventBus> {

    public String onGoToHomeDemandsModule(SearchModuleDataHolder searchModuleDataHolder) {
        if (searchModuleDataHolder == null) {
            return "";
        } else {
            return "filter:" + searchModuleDataHolder.toStringWithIDs();
        }
    }

    public String onCreateTokenForHistory(
            LinkedList<TreeItem> openedHierarchy, int page, FullDemandDetail supplierDetail) {
        StringBuilder token = new StringBuilder();
        //Category
        token.append("tree=");
        token.append(convertCateoryMapToToken(openedHierarchy));
        //Page
        token.append(";page=");
        token.append(page);
        //Demand
        token.append(";supId=");
        token.append(supplierDetail == null ? "-1" : supplierDetail.getDemandId());
        return token.toString();
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
        if (param == null) {
            //aj tak tu nemam categoryDetail ale iba ID
            eventBus.goToHomeDemandsModule(null);
        } else {
            Storage.setCalledDueToHistory(true);
            if (param.startsWith("filter:")) {
                param = param.replace("filter:", "");
                eventBus.goToHomeDemandsModule(SearchModuleDataHolder.parseSearchModuleDataHolder(param));
            } else {
                //When back & forward events -> don't need to call goToHomeDemandModule
                // - it would create new universalAsyncTable, ...
                // - just use what is already created - events will fire appropiate actions
                //parse param
                String[] params = param.split(";");
                LinkedList<TreeItem> tree = convertCategoryTokenToMap(params[0].split("=")[1]);
                if (!tree.isEmpty()) {
                    eventBus.getCategoryAndSetModuleByHistory(
                            tree,
                            tree.getLast().getCategoryId(),
                            Integer.valueOf(params[1].split("=")[1]),
                            Long.valueOf(params[2].split("=")[1]));
                }
            }
        }
    }

    private LinkedList<TreeItem> convertCategoryTokenToMap(String token) {
        LinkedList<TreeItem> tree = new LinkedList<TreeItem>();
        StringBuilder str = new StringBuilder(token);
        str.deleteCharAt(0);
        str.deleteCharAt(str.length() - 1);
        if (!str.toString().isEmpty()) {
            String[] items = str.toString().split(",");
            int level = 1;
            for (String item : items) {
                tree.add(new TreeItem(Long.valueOf(item.split(":")[0]), level, Integer.valueOf(item.split(":")[1])));
                level++;
            }
        }
        return tree;
    }

    private String convertCateoryMapToToken(LinkedList<TreeItem> openedHierarcy) {
        StringBuilder str = new StringBuilder();
        str.append("(");
        if (openedHierarcy != null && !openedHierarcy.isEmpty()) {
            for (TreeItem item : openedHierarcy) {
                str.append(item.getCategoryId());
                str.append(":");
                str.append(item.getIndex());
                str.append(",");
            }
            str.deleteCharAt(str.length() - 1);
        }
        str.append(")");
        return str.toString();
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
