package com.asap.messenger.bo;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Message Java Class is used to store the information related to message that is sent/received/draft etc
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/16/2015
 */
public class Message {

    /*Attributes to store the message information*/
    private int messageId;
    private String messageAddress;
    private String messageContent;
    private long timestamp;
    private String status;
    private String contactName;
    private boolean locked;

    /* Empty Constructor */
    public Message(){

    }

    /*Constructor*/
    public Message(int messageId, String messageContent, String messageAddress, long timestamp, String status){
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.status = status;
        this.messageAddress = messageAddress;
    }

    /*Getters and Setters*/
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public void setMessageAddress(String messageAddress) {
        this.messageAddress = messageAddress;
    }
}
