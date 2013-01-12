/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.google.gwt.view.client.ProvidesKey;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
public class MessageDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -928374659233195109L;
    //UserMessage
    private boolean read = false;
    private boolean starred = false;
    private long userMessageId = -1;
    //Message
    private long messageId = -1;
    private long threadRootId = -1;
    private long parentId = -1;
    private long senderId = -1;
    private String subject = null;
    private String body = null;
    private String messageState = null;
    private String messageType = null;
    private Date created = null;
    private Date sent = null;
    private String senderName;

    public static final ProvidesKey<MessageDetail> KEY_PROVIDER =
            new ProvidesKey<MessageDetail>() {

                @Override
                public Object getKey(MessageDetail item) {
                    return item == null ? null : item.getMessageId();
                }
            };

    public MessageDetail() {
    }

    public MessageDetail(MessageDetail detail) {
        this.updateWholeMessage(detail);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeMessage(MessageDetail detail) {
        //UserMessage
        read = detail.isRead();
        starred = detail.isStarred();
        userMessageId = detail.getUserMessageId();
        //Message
        messageId = detail.getMessageId();
        threadRootId = detail.getThreadRootId();
        parentId = detail.getParentId();
        senderId = detail.getSenderId();

        subject = detail.getSubject();
        body = detail.getBody();
        messageState = detail.getMessageState();
        messageType = detail.getMessageType();
        created = detail.getCreated();
        sent = detail.getSent();
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long id) {
        this.messageId = id;
    }

    /**
     * Return the root message representing one communication thread. Still the same, child messages inherit this
     * id.
     *
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
     * For Root Message messageId equals threadId.
     *
     * @return the parentId
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * For Root Message messageId equals threadId.
     *
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the messageState
     */
    public String getMessageState() {
        return messageState;
    }

    /**
     * @param messageState the messageState to set
     */
    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    // TODO for praso: what's the difference between sent and created date? Is it needed
    /**
     * @return the sent
     */
    public Date getSent() {
        return sent;
    }

    // TODO for praso: what's the difference between sent and created date? Is it needed
    /**
     * @param sent the sent to set
     */
    public void setSent(Date sent) {
        this.sent = sent;
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageDetail{");
        sb.append("read=" + read);
        sb.append(", starred=" + starred);
        sb.append(", userMessageId=" + userMessageId);
        sb.append(", messageId=" + messageId);
        sb.append(", threadRootId=" + threadRootId);
        sb.append(", parentId=" + parentId);
        sb.append(", senderId=" + senderId);
        sb.append(", senderName=" + senderName);
        sb.append(", subject=" + subject);
        sb.append(", body=" + body);
        sb.append(", messageState=" + messageState);
        sb.append(", messageType=" + messageType);
        sb.append(", created=" + (created == null ? "null" : created.toString()));
        sb.append(", sent=" + (sent == null ? "null" : sent.toString()));
        sb.append('}');
        return sb.toString();
    }

    /**
     * @return the senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @param senderName the senderName to set
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
