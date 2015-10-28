package com.asap.messenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shaanu on 10/21/2015.
 */
public class CreateMessageActivity extends AppCompatActivity {
    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createmessage);

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        System.out.println(originalMessageList);

        final EditText newMessageText = (EditText)findViewById(R.id.newMessage);
        EditText contactNameText = (EditText)findViewById(R.id.senderContact);
        Iterator<Message> messageIterator = originalMessageList.iterator();
        while(messageIterator.hasNext()){
            Message message = messageIterator.next();
            if(message.getStatus().equals(MessageStatus.NEW)){
                newMessageText.setText(message.getMessageContent());
                contactNameText.setText(message.getReceiver().get(0).getPhoneNumber());
                newMessageText.setTextColor(Color.BLACK);
                messageIterator.remove();
            }
        }

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            System.out.println("In If create");
            String messageToForward = getIntent().getExtras().getString("messageToForward");
            if(messageToForward!=null){
                newMessageText.setText(messageToForward);
                newMessageText.setTextColor(Color.BLACK);
            }else{
                newMessageText.setHint("Type Message");
                newMessageText.setTextColor(Color.GRAY);
            }
        }else if(newMessageText.getText().toString().equals("")){
            newMessageText.setHint("Type Message");
            newMessageText.setTextColor(Color.GRAY);
        }

        newMessageText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            newMessageText.setTextColor(Color.BLACK);
            return false;
            }
        });

        Button sendButton = (Button)findViewById(R.id.Button);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent("com.asap.messenger.sendmessage");

                EditText senderContactText = (EditText)findViewById(R.id.senderContact);
                String senderContact = senderContactText.getText().toString();

                EditText newMessageText = (EditText)findViewById(R.id.newMessage);
                String message = newMessageText.getText().toString();

                sendSMS(senderContact, message, sendIntent);
            }
        });
    }

    private void sendSMS(String number, String message, Intent send) {
        PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, send, 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, sendPI, null);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent();
        setIntent.setClassName("com.asap.messenger", "com.asap.messenger.ViewAllMessagesActivity");
        EditText newMessageText = (EditText)findViewById(R.id.newMessage);
        String newMessage = newMessageText.getText().toString();
        System.out.println("on Back button pressed :" +newMessage);

        EditText newContactText = (EditText)findViewById(R.id.senderContact);
        String newContact = newContactText.getText().toString();

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        appState.setMessageList(originalMessageList);
        originalMessageList.add(new Message(52, newMessage, "111-111-1111" , newContact, "10-17-2015", MessageStatus.NEW));
        startActivity(setIntent);
    }
}