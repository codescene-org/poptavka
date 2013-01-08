/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import java.util.Date;

/**
 * This interface represent universal detail used in UniversalTableWidget.
 * It contains all needed method definitions for all possible cases.
 * Each detail object what is going to be used with UniversalTableWidget must implement it.
 *
 * @author Martin Slavkovsky
 */
public interface IUniversalDetail {

    // Client part
    //--------------------------------------------------------------------------
    long getClientId();

    String getClientName();

    // Supplier part
    //--------------------------------------------------------------------------
    long getSupplierId();

    String getSupplierName();

    int getRating();

    // Message part
    //--------------------------------------------------------------------------
    long getMessageId();

    long getThreadRootId();

    long getSenderId();

    Date getMessageSent();

    // UserMessage part
    //--------------------------------------------------------------------------
    long getUserMessageId();

    boolean isRead();

    void setRead(boolean isRead);

    boolean isStarred();

    void setStarred(boolean isStarred);

    int getMessageCount();

    int getUnreadMessageCount();

    Date getDeliveryDate();

    // Demand part
    //--------------------------------------------------------------------------
    long getDemandId();

    Date getValidTo();

    Date getEndDate();

    Date getReceivedDate();

    String getTitle();

    String getPrice();

    // Offer part
    //--------------------------------------------------------------------------
    long getOfferId();

    // Display
    //--------------------------------------------------------------------------
    int CLIENT_NAME = 0;
    int SUPPLIER_NAME = 1;
    /**
     * Return CLIENT or SUPPLIER name with count of sub.
     *
     * @param displayWhat - constants: IUniversalDetail.CLIENT_NAME, IUniversalDetail.SUPPLIER_NAME
     * @return
     */
    String displayUserNameWithUnreadMessageCounts(int displayWhat);
}
