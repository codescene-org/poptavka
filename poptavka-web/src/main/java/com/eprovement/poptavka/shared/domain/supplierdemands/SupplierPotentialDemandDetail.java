/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.supplierdemands;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Ivan
 */
public class SupplierPotentialDemandDetail implements Serializable {

    private static final long serialVersionUID = -6019479783491937543L;

    // Client part
    private long clientId;
    private String clientName; // column 2

    // Message part
    private long messageId;
    private long threadRootId;
    private long senderId;
    private Date messageSent; // column 5

    // UserMessage part
    private long userMessageId;
    private boolean isRead;
    private boolean isStarred; // column 1
    private int messageCount; // all messages between Supplier and Client regarding this potential demand
    private int unreadMessageCount; // number of Supplier's unread messages regarding this potential demand

    // Demand part
    private long demandId;
    private Date validTo;
    private Date endDate; // column 4 - I believe this field is used to make urgency icon in our table
    private String title; // column 3
    private String price; // column ? - maybe we will not display this in table

    /**
     * @return the clientId
     */
    public long getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the messageId
     */
    public long getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the threadRootId
     */
    public long getThreadRootId() {
        return threadRootId;
    }

    /**
     * @param threadRootId the threadRootId to set
     */
    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    /**
     * @return the senderId
     */
    public long getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the messageSent
     */
    public Date getMessageSent() {
        return messageSent;
    }

    /**
     * @param messageSent the messageSent to set
     */
    public void setMessageSent(Date messageSent) {
        this.messageSent = messageSent;
    }

    /**
     * @return the userMessageId
     */
    public long getUserMessageId() {
        return userMessageId;
    }

    /**
     * @param userMessageId the userMessageId to set
     */
    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /**
     * @return the isRead
     */
    public boolean isIsRead() {
        return isRead;
    }

    /**
     * @param isRead the isRead to set
     */
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * @return the isStarred
     */
    public boolean isIsStarred() {
        return isStarred;
    }

    /**
     * @param isStarred the isStarred to set
     */
    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    /**
     * @return the messageCount
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * @param messageCount the messageCount to set
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * @return the unreadMessageCount
     */
    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    /**
     * @param unreadMessageCount the unreadMessageCount to set
     */
    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    /**
     * @return the demandId
     */
    public long getDemandId() {
        return demandId;
    }

    /**
     * @param demandId the demandId to set
     */
    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /**
     * @return the validTo
     */
    public Date getValidTo() {
        return validTo;
    }

    /**
     * @param validTo the validTo to set
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

}