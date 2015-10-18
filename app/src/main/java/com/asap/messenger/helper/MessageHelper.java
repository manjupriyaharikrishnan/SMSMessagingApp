package com.asap.messenger.helper;

import com.asap.messenger.bo.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umadevi on 10/17/2015.
 */
public class MessageHelper {

    public List<Message> getAllMessages(){
        List<Message> messagesList = new ArrayList<Message>();
        messagesList.add(new Message(1, "Hello How are you ?", "111-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "222-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "333-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "444-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "555-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "666-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "777-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "888-222-2222", "111-111-1111", "10-17-2015"));
        messagesList.add(new Message(1, "Hello How are you ?", "999-222-2222", "111-111-1111", "10-17-2015"));
        return messagesList;
    }
}
