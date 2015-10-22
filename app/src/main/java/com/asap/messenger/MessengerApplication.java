package com.asap.messenger;

import android.app.Application;

import com.asap.messenger.bo.Message;

import java.util.List;

/**
 * Created by Umadevi on 10/22/2015.
 */
public class MessengerApplication extends Application{

    private List<Message> messageList;

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
