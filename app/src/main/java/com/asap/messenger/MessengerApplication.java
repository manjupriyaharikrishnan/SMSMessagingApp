package com.asap.messenger;

import android.app.Application;

import com.asap.messenger.bo.Message;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Umadevi on 10/22/2015.
 */
public class MessengerApplication extends Application{

    private List<Message> messageList;

    private HashMap<String, String> phoneContacts;

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public HashMap<String, String> getPhoneContacts() {
        return phoneContacts;
    }

    public void setPhoneContacts(HashMap<String, String> phoneContacts) {
        this.phoneContacts = phoneContacts;
    }
}
