package com.asap.messenger;

import android.app.Application;

import com.asap.messenger.bo.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Umadevi on 10/22/2015.
 */
public class MessengerApplication extends Application{

    private List<Message> messageList;

    private List<Message> draftsList;

    private HashMap<String, String> phoneContacts;

    public List<Message> getDraftsList() {
        if(draftsList!=null){
            return draftsList;
        }else{
            return new ArrayList<Message>();
        }
    }

    public void setDraftsList(List<Message> draftsList) {
        this.draftsList = draftsList;
    }

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
