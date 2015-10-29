package com.asap.messenger.bo;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umadevi on 10/16/2015.
 */
public class Message {
    private int messageId;
    private String messageContent;
    private Sender sender;
    private List<Receiver> receiver;
    private String timestamp;
    private String status;
    private String contactName;

    public Message(){

    }

    public Message(int messageId, String messageContent, String senderContact, String receiverContact, String timestamp, String status){
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.status = status;

        Sender sender = new Sender();
        sender.setPhoneNumber(senderContact);
        sender.setContactName(senderContact);
        this.sender = sender;

        Receiver receiver = new Receiver();
        receiver.setPhoneNumber(receiverContact);
        receiver.setContactName(receiverContact);
        List<Receiver> receiverList = new ArrayList<Receiver>();
        receiverList.add(receiver);
        this.receiver = receiverList;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public List<Receiver> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<Receiver> receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

}
