package com.asap.messenger.helper.test;

import com.asap.messenger.bo.Message;
import com.asap.messenger.helper.MessageHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Umadevi on 10/20/2015.
 */
public class MessageHelperTest {

    MessageHelper messageHelper = new MessageHelper();

    @Test
    public void testGetAllMessages() {
        assertNotNull(messageHelper.getAllMessages());
    }

    @Test
    public void testGetMessagesByContact(){
        //assertNotNull(messageHelper.getMessagesByContact("Hello"));
    }

    @Test
    public void testGetDateForDisplay() {
        assertEquals("Oct 20", MessageHelper.getDateForDisplay("10-20-2015"));
    }
}
