package com.asap.messenger.helper;

import android.database.Cursor;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The MessageHelper Class is used as a helper class for doing the activities on the messages like getting the messages from inbox,
 * getting the latest messages, getting messages by contact, sorting messages etc
 * @author  Umadevi Samudrala,Karthika J
 * @version 1.0
 * @since 10/17/2015
 */
public class MessageHelper {

    /**
     * Getting the messages from the SMS Inbox
     * @param messageList List of the messages
     * @param cursor The Cursor provides random read-write access to the result set returned by a database query.
     * @return List of the messages after setting the attributes of the messages retrieved from the SMS Inbox
     */
    public List<Message> getMessagesFromInbox(List<Message> messageList, Cursor cursor){

        System.out.println("In Message Helper.. getMessagesFromInbox");
        if(messageList==null){
            messageList = new ArrayList<Message>();
        }
        // Iterating over the cursor to access each message
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                // Getting the message attributes from the cursor and storing in local variables
                String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String messageAddress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                long messageDate = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                String messageType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String messageStatus = " ";
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

               /*
                Checking the status of the messages
                SMS Status 1-Received, 2-Sent, 3-Draft
                */
                if (messageType.contentEquals("1")) messageStatus = MessageStatus.RECEIVED;
                else if (messageType.contentEquals("2")) messageStatus = MessageStatus.SENT;
                else if (messageType.contentEquals("3"))  messageStatus = MessageStatus.DRAFT;

                /*
                generally port number on which emulator runs is 5554, 5556 which is only 4 digits
                Appending the value 1555521 as the prefix to the port number to frame the contact number
                 */
                StringBuilder sbMessageAddress = new StringBuilder(messageAddress);
                if(sbMessageAddress.length()==4){
                    sbMessageAddress.insert(0, "1555521");
                }

                Message tmpMsg = new Message(id, messageBody, sbMessageAddress.toString(), messageDate, messageStatus);
                System.out.println("Message Type is "+messageType);
                messageList.add(tmpMsg);
            } while (cursor.moveToNext());
        } else {
            System.out.println("No SMS .... in Inbox");
            // empty box, no SMS
        }
        return messageList;
    }

    /**
     * Method to get the latest messages for all the contacts and messages segregated according to contact number
     * If the contact number is stored in Stock App, Identify it and map the contact number to the Contact name of stocks contacts app
     * @param originalMessageList The original message list from the SMS Inbox
     * @param phoneContacts The list of contacts from the Stock Contacts App
     * @return The List of latest messages for each contact number sorted by timestamp and contact number
     */
    public List<Message> getLatestMessagesByAllContacts(List<Message> originalMessageList, HashMap<String, String> phoneContacts){

        List<Message> sortedList = new ArrayList<Message>();

        // Grouping the messages according to the contact number. Also check if the contact number is in Stock Contacts App
        HashMap<String, List<Message>> sortedMap = new HashMap<String, List<Message>>();
        for(Message originalMessage : originalMessageList){
            String contact = originalMessage.getMessageAddress();
            if(phoneContacts.containsKey(contact)){
                originalMessage.setContactName(phoneContacts.get(contact));
            }
            List<Message> mapValue;
            if(sortedMap.containsKey(contact)){
                mapValue = sortedMap.get(contact);
            }else{
                mapValue = new ArrayList<Message>();
            }
            mapValue.add(originalMessage);
            sortedMap.put(contact, mapValue);
        }

        // Sorting the messages by timestamp
        for (List<Message> list : sortedMap.values()) {
            Message latestMessage = null;
            long latestDate = 0;
            for(Message listMessage : list){
                long milliSec = listMessage.getTimestamp();
                if(milliSec > latestDate){
                    latestDate = milliSec;
                    latestMessage = listMessage;
                }
            }
            sortedList.add(latestMessage);
        }
        return sortedList;
    }

    /**
     * Method to get the long value for the input date
     * @param inputDate Format MM-dd-yyyy HH:mm:ss
     * @return time in ms
     */
    private long getLongValueOfDate(String inputDate){
        SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date dateObj;
        try {
            dateObj = f.parse(inputDate);
            return dateObj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieve messages by using contact number
     * @param contact Contact number whose messages need to be retrieved
     * @param originalMessageList Messages from inbox
     * @return List of messages for that contact number
     */
    public List<Message> getMessagesByContact(String contact, List<Message> originalMessageList){

        // Checking if the message is related to the contact
        List<Message> messagesList = new ArrayList<Message>();
        for(Message message : originalMessageList){
            if(message.getMessageAddress().equals(contact)){
                messagesList.add(message);
            }
        }

        // Sorting the messages by using timestamp
        Collections.sort(messagesList, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return Long.compare(message1.getTimestamp(), message2.getTimestamp());
            }
        });

        return messagesList;
    }

    /**
     * Check if the message is locked. Message is locked if the locked attribute is set
     * @param id message id
     * @param originalMessageList Message from inbox
     * @return true or false
     */
    public boolean checkIfMessageIsLocked(int id, List<Message> originalMessageList){
        for(Message message : originalMessageList){
            if(message.getMessageId()==id && message.isLocked()){
                return true;
            }
        }
        return false;
    }

    /**
     * Delete the message by using id
     * @param id Message id to be deleted
     * @param originalMessageList Messages from inbox
     * @return List of messages after deletion
     */
    public List<Message> deleteMessageById(int id, List<Message> originalMessageList){
        for(Message message : originalMessageList){
            if(message.getMessageId()==id){
                originalMessageList.remove(message);
                return originalMessageList;
            }
        }
        return originalMessageList;
    }

    /**
     * Method to convert the date in ms to the String Date
     * @param inputDate in ms
     * @return Formatted Date string
     */
    public static String getDateForDisplay(long inputDate){
        Date longDate = new Date(inputDate);
        String formattedDate;

        // IF the day is today, just display the time
        // If the day is not today, display the date
        if(isToday(longDate)){
            DateFormat targetFormat = new SimpleDateFormat("h:mm a");
            formattedDate = targetFormat.format(longDate);
            return formattedDate;
        }else{
            DateFormat targetFormat = new SimpleDateFormat("MMM dd");
            formattedDate = targetFormat.format(longDate);
            return formattedDate;
        }
    }

    /**
     * Check if the given date is today
     * @param givenDate Input date
     * @return true or false
     */
    public static boolean isToday(Date givenDate){

        Calendar givenDateCal = Calendar.getInstance();
        givenDateCal.setTime(givenDate);

        Calendar todayDateCal = Calendar.getInstance();
        todayDateCal.setTime(new Date());

        return givenDateCal.get(Calendar.ERA) == todayDateCal.get(Calendar.ERA)
                && givenDateCal.get(Calendar.YEAR) == todayDateCal.get(Calendar.YEAR)
                && givenDateCal.get(Calendar.DAY_OF_YEAR) == todayDateCal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Check if the input string is numeric
     * @param str Input String
     * @return true of false
     */
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
