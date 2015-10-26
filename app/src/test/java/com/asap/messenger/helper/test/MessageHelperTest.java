package com.asap.messenger.helper.test;

import android.support.annotation.NonNull;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Umadevi on 10/20/2015.
 */
public class MessageHelperTest {

    MessageHelper messageHelper = new MessageHelper();

    //To test the message field is not null

    @Test
    public void testGetAllMessages() {
        assertNotNull(messageHelper.getAllMessages());
    }

// To test the GetMessageByContact
    @Test
    public void testGetMessagesByContact(){

        //assertNotNull(messageHelper.getMessagesByContact("111-111-1111"));
        List<Message> sample=new ArrayList<>();
        sample.add(new Message(10,"Hello How are you ?", "111-222-2222", "111-111-1111", "10-17-2015", MessageStatus.RECEIVED));
        assertNotNull(messageHelper.getMessagesByContact("aaaa", sample));
    }

    @Test
    public void testGetMessagesByContactwithnullfield(){

        //assertNotNull(messageHelper.getMessagesByContact("111-111-1111"));
        List<Message> sample=new ArrayList<>();
        sample.add(new Message(10,"Hello How are you ?", "111-222-2222", "10-17-2015", MessageStatus.RECEIVED,"111-111-1111"));
        assertNull(messageHelper.getMessagesByContact("aaaa", sample));
    }


    // To test the display date format
    @Test
    public void testGetDateForDisplay() {
        assertEquals("Oct 20", MessageHelper.getDateForDisplay("10-20-2015"));
    }

    @Test
    public void testGetDateForDisplaynotrightformat() {
        assertNotEquals("Oct 20", MessageHelper.getDateForDisplay("10-2015-20"));
    }

    @Test
    public void testGetDateForDisplaynotrightformatmonth() {
        assertNotEquals("Oct 20", MessageHelper.getDateForDisplay("1015-20-20"));
    }


}
