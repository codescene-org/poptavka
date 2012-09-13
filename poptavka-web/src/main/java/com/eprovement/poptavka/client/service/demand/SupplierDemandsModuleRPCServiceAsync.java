package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public interface SupplierDemandsModuleRPCServiceAsync {

    //SupplierDemands widget
    void getSupplierPotentialDemandsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getSupplierPotentialDemands(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    //SupplierOffers widget
    void getSupplierOffersCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getSupplierOffers(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    //SupplierAssignedDemands widget
    void getSupplierAssignedDemandsCount(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<Long> callback);

    void getSupplierAssignedDemands(long supplierID, SearchDefinition searchDefinition,
            AsyncCallback<List<FullOfferDetail>> callback);

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    void getFullDemandDetail(long demandId, AsyncCallback<FullDemandDetail> callback);

    void getFullSupplierDetail(long supplierId, AsyncCallback<FullSupplierDetail> callback);

    void getSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId, AsyncCallback<ArrayList<MessageDetail>> callback);

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    void setMessageReadStatus(List<Long> userMessageIds, boolean isRead, AsyncCallback<Void> callback);

    void setMessageStarStatus(List<Long> list, boolean newStatus, AsyncCallback<Void> callback);

    void finishOffer(FullOfferDetail fullOfferDetail, AsyncCallback<Void> callback);
}
