package com.asap.messenger.helper.test;

import android.renderscript.Long2;
import android.support.annotation.NonNull;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import junit.framework.Assert;

import static com.asap.messenger.helper.MessageHelper.isToday;
import static org.junit.Assert.*;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Umadevi on 10/20/2015.
 */
public class MessageHelperTest {

    MessageHelper messageHelper = new MessageHelper();


    @Test
    public void testdeletemessagebyId() {
        List<Message> originalMessagelist = new ArrayList<Message>();
        for (Message msg : originalMessagelist) {
            if (msg.getMessageId() == 2)
                originalMessagelist.remove(msg);
            assertNotNull(originalMessagelist);
        }
    }

    @Test
    public void testnotdeletemessagebyId() {
        int a = 0;
        List<Message> originalMessagelist = new ArrayList<Message>();
        for (Message msg : originalMessagelist) {
            if (msg.getMessageId() == a)
                originalMessagelist.remove(msg);
            assertNull(originalMessagelist);
        }
    }


    @Test
    public void testcheckifmsgislocked() {
        List<Message> originalMessagelist = new ArrayList<Message>();
        for (Message msg : originalMessagelist) {
            if (msg.getMessageId() == 2 && msg.isLocked()) {
                assertTrue(true);
                assertNotEquals(false, true);

            }
        }
    }

    @Test
    public void testisNumeric() {
        assertFalse(MessageHelper.isNumeric(("111-111-1111")));
    }

    @Test
    public void testisnotNumeric() {
        assertFalse(MessageHelper.isNumeric(("a111111111")));
    }

    @Test
    public void testisToday() {
        Date givendate = new Date(07 / 11 / 2015);
        Calendar givenDateCal = Calendar.getInstance();
        givenDateCal.setTime(givendate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());


        if (givenDateCal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA) &&
                givenDateCal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givenDateCal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR)) {
            assertTrue(true);
        }
    }

    @Test
    public void testisnotToday() {
        Date givendate = new Date(07 / 11 / 2015);
        Calendar givenDateCal = Calendar.getInstance();
        givenDateCal.setTime(givendate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());


        if (givenDateCal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA) &&
                givenDateCal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givenDateCal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR)) {
            assertFalse(true);
        }
    }


    @Test
    public void testisnotcorrectToday() {
        Date givendate = new Date(07 / 2011 / 15);
        Calendar givenDateCal = Calendar.getInstance();
        givenDateCal.setTime(givendate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());


        if (givenDateCal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA) &&
                givenDateCal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givenDateCal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR)) {
            assertFalse(true);
        }
    }

    @Test
    public void testgetdatefordisplay() {
        Date longDate = new Date(07 / 11 / 2015);
        String formattedDate = null;

        if (isToday(longDate)) {
            DateFormat targetFormat = new SimpleDateFormat("2:15 a");
            formattedDate = targetFormat.format(longDate);
            assertNotNull(formattedDate);
        } else {
            DateFormat targetFormat = new SimpleDateFormat("02 03");
            formattedDate = targetFormat.format(longDate);
            assertNotNull(formattedDate);
        }
    }


    @Test
    public void testgetmessagebycontact() {
        List<Message> originalMessagelist = new ArrayList<Message>();
        List<Message> Messagelist = new ArrayList<Message>();

        String contact = "aaa";
        for (Message msg : originalMessagelist) {
            if (msg.getMessageAddress().equals(contact)) {
                Boolean a = Messagelist.add(msg);
                assertFalse(a);
            }
        }

    }
}