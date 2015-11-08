package com.asap.messenger.helper;

import android.database.Cursor;

import com.asap.messenger.bo.Message;
import com.asap.messenger.bo.Receiver;
import com.asap.messenger.common.MessageStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Umadevi on 10/17/2015.
 */
public class MessageHelper {

    public List<Message> getAllMessagesOld(){
        List<Message> messagesList = new ArrayList<Message>();
        messagesList.add(new Message(1, "Hello 111 How are you ?", "111-222-3333", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(2, "Hello 222 How are you ?", "222-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(3, "Hello 333 How are you ?", "333-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(4, "Hello 444 How are you ?", "444-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(5, "Hello 555 How are you ?", "555-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(6, "Hello 666 How are you ?", "666-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(7, "Hello 777How are you ?", "777-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(8, "Hello 888 How are you ?", "888-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(9, "Hello 999 How are you ?", "999-222-2222", "111-111-1111", "10-17-2015 02:30:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(10, "Am fine, How are you", "111-111-1111", "999-222-2222", "10-17-2015 02:32:00", MessageStatus.SENT));
        messagesList.add(new Message(11, "Joining for movie today", "999-222-2222", "111-111-1111", "10-17-2015 02:33:00", MessageStatus.RECEIVED));
        messagesList.add(new Message(12, "Sure, Meet you at 2", "111-111-1111", "999-222-2222", "10-17-2015 02:34:00", MessageStatus.SENT));
        return messagesList;
    }

    public List<Message> getMessagesFromInbox(List<Message> messageList, Cursor cursor){
        if(messageList==null){
            messageList = new ArrayList<Message>();
        }
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                int idx = 0;
                String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                String messageAddress = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                long messageDate = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                String messageType = cursor.getString(cursor.getColumnIndexOrThrow("type")).toString();
                String messageStatus = " ";
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                Date dateFromSms = new Date(messageDate);
                String messageDateString = formatter.format(dateFromSms);

                if (messageType.contentEquals("1")) messageStatus = MessageStatus.RECEIVED;
                else if (messageType.contentEquals("2")) messageStatus = MessageStatus.SENT;
                else if (messageType.contentEquals("3"))  messageStatus = MessageStatus.DRAFT;

                Message tmpMsg = new Message(idx++,messageBody,messageAddress,"Receiver",messageDateString,messageStatus);
                System.out.println("Message Type is "+tmpMsg.getStatus());
                messageList.add(tmpMsg);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        return messageList;
    }

    public void setMessageList()
    {
 //       List<Message> originalMessageList
    }

    public List<Message> getLatestMessagesByAllContacts(List<Message> originalMessageList, HashMap<String, String> phoneContacts){

        List<Message> sortedList = new ArrayList<Message>();

        HashMap<String, List<Message>> sortedMap = new HashMap<String, List<Message>>();
        for(Message originalMessage : originalMessageList){
            String contact = null;
            if(MessageStatus.RECEIVED.contentEquals(originalMessage.getStatus())){
               contact = originalMessage.getSender().getPhoneNumber();
            }else if(MessageStatus.SENT.contentEquals(originalMessage.getStatus())){
                contact = originalMessage.getReceiver().get(0).getPhoneNumber();
            }else{
                break;
            }
            if(phoneContacts.containsKey(contact)){
                System.out.println("Key Present..."+contact);
                originalMessage.setContactName(phoneContacts.get(contact));
            }
            List<Message> mapValue = null;
            if(sortedMap.containsKey(contact)){
                mapValue = sortedMap.get(contact);
            }else{
                mapValue = new ArrayList<Message>();
            }
            mapValue.add(originalMessage);
            sortedMap.put(contact, mapValue);
        }

        for (List<Message> list : sortedMap.values()) {
            Message latestMessage = null;
            long latestDate = 0;
            for(Message listMessage : list){
                long milliSec = getLongValueOfDate(listMessage.getTimestamp());
                if(milliSec > latestDate){
                    latestDate = milliSec;
                    latestMessage = listMessage;
                }
            }
            sortedList.add(latestMessage);
        }
        return sortedList;
    }

    private long getLongValueOfDate(String inputDate){
        SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date dateObj = null;
        try {
            dateObj = f.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = dateObj.getTime();
        return milliseconds;
    }

    public List<Message> getMessagesByContact(String contact, List<Message> originalMessageList){
        List<Message> messagesList = new ArrayList<Message>();
        for(Message message : originalMessageList){
            if(message.getSender().getPhoneNumber().equals(contact)){
                messagesList.add(message);
            }
            for(Receiver receiver : message.getReceiver()){
                if(receiver.getPhoneNumber().equals(contact)){
                    messagesList.add(message);
                }
            }
        }

        return messagesList;
    }

    public boolean checkIfMessageIsLocked(int id, List<Message> originalMessageList){
        for(Message message : originalMessageList){
            if(message.getStatus().equals(MessageStatus.LOCK)){
                return true;
            }
        }
        return false;
    }

    public List<Message> deleteMessageById(int id, List<Message> originalMessageList){
        for(Message message : originalMessageList){
            if(message.getMessageId()==id){
                originalMessageList.remove(message);
                return originalMessageList;
            }
        }
        return originalMessageList;
    }

    public static String getDateForDisplay(String inputDate){
        DateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat targetFormat = new SimpleDateFormat("MMM dd");
        try{
            Date date = originalFormat.parse(inputDate);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        }catch(ParseException e){
            return null;
        }
    }
}
