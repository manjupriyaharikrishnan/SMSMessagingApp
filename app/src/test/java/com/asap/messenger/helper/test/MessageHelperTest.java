package com.asap.messenger.helper.test;

import android.database.Cursor;
import android.renderscript.Long2;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import junit.framework.Assert;

import static com.asap.messenger.helper.MessageHelper.getDateForDisplay;
import static com.asap.messenger.helper.MessageHelper.isToday;
import static org.junit.Assert.*;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import dalvik.annotation.TestTarget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Umadevi on 10/20/2015.
 * The MessageHelperTest class is the unit test cases for Message helper class.
 * @author  Deepa Raveendran, Manju Priya Hari Krishnan
 */

public class MessageHelperTest {

    MessageHelper messageHelper = new MessageHelper();

    @Test
    public void testisnotNumericwithstring() {
        String str = new String();
        str = "111-222-aaaa";
        assertFalse(MessageHelper.isNumeric((str)));
    }

    @Test
    public void testisnotNumericwithspecialcharacters() {
        String str = new String();
        str = "111-$$$-aaaa";
        assertFalse(MessageHelper.isNumeric((str)));
    }


    @Test
    public void testisNumeric() {
        String str = new String();
        str = "1112223333";
        assertTrue(MessageHelper.isNumeric((str)));
    }

    @Test
    public void testisnotNumericwithhyphen() {
        String str = new String();
        str = "111-222-3333";
        assertFalse(MessageHelper.isNumeric((str)));
    }

    @Test
    public void testgetdatefordisplaycase2() {
        MessageHelper messageHelper = new MessageHelper();
        long inputDate = 01 - 02;
        Date longDate = new Date(inputDate);
        String formatteddate = null;
        Boolean a = isToday(longDate);

        DateFormat targetFormat = new SimpleDateFormat("11 09");
        formatteddate = targetFormat.format(longDate);

        assertNotNull(messageHelper.getDateForDisplay(inputDate), a);

    }

    @Test
    public void testgetdatefordisplayequalvalue() {
        MessageHelper messageHelper = new MessageHelper();
        long inputDate = 01 - 02;
        Date longDate = new Date(inputDate);
        String formatteddate = null;
        Boolean a = isToday(longDate);
        DateFormat targetFormat = new SimpleDateFormat("11 09");
        formatteddate = targetFormat.format(longDate);

        assertNotEquals(messageHelper.getDateForDisplay(inputDate), a);

    }

    @Test
    public void testisTodaynotsame() throws ParseException {

        String inputStr = "11-11-2012";
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date givenDate = dateFormat.parse(inputStr);

        Calendar givendatecal = Calendar.getInstance();
        givendatecal.setTime(givenDate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());

        if (givendatecal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA)
                && givendatecal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givendatecal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR))
            ;
        {

            assertFalse(messageHelper.isToday(givenDate));
        }
    }

    @Test
    public void testisTodaysame() throws ParseException {

        String inputStr = "11-15-2015";
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date givenDate = dateFormat.parse(inputStr);

        Calendar givendatecal = Calendar.getInstance();
        givendatecal.setTime(givenDate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());

        if (givendatecal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA)
                && givendatecal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givendatecal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR))
            ;
        {

            assertFalse(messageHelper.isToday(givenDate));
        }
    }

    @Test
    public void testisTodaynotsamewithstring() throws ParseException {

        String inputStr = "11-15-20aa";
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date givenDate = dateFormat.parse(inputStr);

        Calendar givendatecal = Calendar.getInstance();
        givendatecal.setTime(givenDate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());

        if (givendatecal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA)
                && givendatecal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givendatecal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR))
            ;
        {

            assertFalse(messageHelper.isToday(givenDate));
        }
    }


    @Test
    public void testdeleteMessageById()
    {
        int id=1;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist=new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()){
            Message str = iter.next();
            if (str.getMessageId() == id) {
                iter.remove();
            }
        }
        assertEquals(messageHelper.deleteMessageById(id,templist),originalMessageList);
    }

    @Test
    public void testdeleteMessageByIdNotPresent()
    {
        int id=3;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist=new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()){
            Message str = iter.next();
            if(str.getMessageId()==id){
                iter.remove();
            }
        }
        assertEquals(messageHelper.deleteMessageById(id, templist), originalMessageList);
    }




    @Test
    public void testdeleteMessageByIdNusingsame()
    {
        int id=3;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist=new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()){
            Message str = iter.next();
            if(str.getMessageId()==id){
                iter.remove();
            }
        }
        assertNotSame(messageHelper.deleteMessageById(id, templist), originalMessageList);
    }



    @Test
    public void getMessagesbycontact() {
        String contact = "5556";
        List<Message> messagesList = new ArrayList<Message>();
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5553", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5555", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist = new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()) {
            Message str = iter.next();
            if (str.getMessageAddress() == contact) {
                messagesList.add(str);
            }
        }
        assertNotSame(messageHelper.getMessagesByContact(contact, templist), originalMessageList);
    }

    @Test
    public void getMessagesbycontact2() {
        String contact = "5553";
        List<Message> messagesList = new ArrayList<Message>();
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5553", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5555", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist = new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()) {
            Message str = iter.next();
            if (str.getMessageAddress() == contact) {
                messagesList.add(str);
            }
        }
        assertNotSame(messageHelper.getMessagesByContact(contact, templist), originalMessageList);
    }

    @Test
    public void getMessagesbycontact3() {
        String contact = "5555";
        List<Message> messagesList = new ArrayList<Message>();
        List<Message> originalMessageList = new ArrayList<>();
        //   originalMessageList.add(new Message(0, "hello", "5553", new Date().getTime(), MessageStatus.LOCK));
        //  originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5555", new Date().getTime(), MessageStatus.SENT));
        List<Message> templist = new ArrayList<>();
        templist.addAll(originalMessageList);
        Iterator<Message> iter = originalMessageList.iterator();
        while (iter.hasNext()) {
            Message str = iter.next();
            if (str.getMessageAddress() == contact) {
                messagesList.add(str);
            }
        }
        assertEquals(messageHelper.getMessagesByContact(contact, templist), originalMessageList);
    }

    @Test
    public void testCheckIfMessageIsLocked(){
        int id=0;
        Boolean result=false;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        for(Message message : originalMessageList){
            if(message.getMessageId()==id && message.isLocked()){
                result= true;
            }
        }
        result=false;
        assertEquals(messageHelper.checkIfMessageIsLocked(id, originalMessageList), result);
    }

    @Test
    public void testCheckIfMessageIsNotLocked(){
        int id=2;
        Boolean result=false;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        for(Message message : originalMessageList){
            if(message.getMessageId()==id && message.isLocked()){
                result= true;
            }
        }
        result=false;
        assertEquals(messageHelper.checkIfMessageIsLocked(id,originalMessageList),result);
    }

    @Test
    public void testCheckIfMessageIsLockedNoId(){
        int id=3;
        Boolean result=false;
        List<Message> originalMessageList = new ArrayList<>();
        originalMessageList.add(new Message(0, "hello", "5554", new Date().getTime(), MessageStatus.LOCK));
        originalMessageList.add(new Message(1, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        originalMessageList.add(new Message(2, "hello", "5554", new Date().getTime(), MessageStatus.SENT));
        for(Message message : originalMessageList){
            if(message.getMessageId()==id && message.isLocked()){
                result= true;
            }
        }
        result=false;
        assertEquals(messageHelper.checkIfMessageIsLocked(id,originalMessageList),result);
    }
}



