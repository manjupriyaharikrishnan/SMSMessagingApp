package com.asap.messenger.helper;

/**
 * The ReceiveMessage Class is used as a broadcast Receiver class.
 * It is a class which has callback methods to be called when the message is received
 * @author  Umadevi Samudrala,Karthika J
 * @version 1.0
 * @since 11/7/2015
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v7.app.NotificationCompat;
import android.app.TaskStackBuilder;
import com.asap.messenger.ConversationViewActivity;

import com.asap.messenger.CreateMessageActivity;
import com.asap.messenger.MessengerApplication;
import com.asap.messenger.R;
import com.asap.messenger.ViewAllMessagesActivity;
import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;

import java.util.HashMap;
import java.util.List;

public class ReceiveMessage extends BroadcastReceiver
{
    /**
     * Method called when the message is received.
     * @param context
     * @param intent
     */

    private String TAG = ReceiveMessage.class.getSimpleName();
    private int numMessagesOne = 0;
    @Override
    public void onReceive(Context context, Intent intent)
    {
 //       int numMessagesOne =   0;
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        List <Message> messageList = null;
        HashMap<String, String>  phoneContacts = null;
        String str = "";
        // Get the messages from the SMS Inbox
        MessengerApplication appState = (MessengerApplication) context.getApplicationContext();
        messageList = appState.getMessageList();
        phoneContacts = appState.getPhoneContacts();

        if (bundle != null)
        {


            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";

                String contact = msgs[i].getOriginatingAddress();
                NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle("Message from  " + msgs[i].getOriginatingAddress());

                if(phoneContacts.containsKey(contact)){
                    mBuilder.setContentTitle("Message from  " + phoneContacts.get(contact));
                }
     //           DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
     //           Date date = new Date();
     //           System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
                mBuilder.setContentText(msgs[i].getMessageBody().toString());
                mBuilder.setTicker("ASAP Message Recieved");
                mBuilder.setSmallIcon(R.drawable.ic_logo_notification);

                // Increase notification number every time a new notification arrives
                mBuilder.setNumber(++numMessagesOne);

                Message tmpMsg = new Message(messageList.size()+1, msgs[i].getMessageBody().toString(), msgs[i].getOriginatingAddress(), msgs[i].getTimestampMillis(), MessageStatus.RECEIVED);

                messageList.add(tmpMsg);
                appState.setMessageList(messageList);

                String selectedContact = msgs[i].getOriginatingAddress();

                Intent notificationIntent = new Intent("com.asap.messenger.conversationview");
                notificationIntent.putExtra("selectedContact", selectedContact);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 100,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // start the activity when the user clicks the notification text
                mBuilder.setContentIntent(contentIntent);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//            myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // pass the Notification object to the system
                notificationManager.notify(0, mBuilder.build());
                abortBroadcast();

         //       startActivity(intent);


            }
            //---display the new SMS message---
          //  Log.d(TAG, str);
          //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show();




        }
    }
}