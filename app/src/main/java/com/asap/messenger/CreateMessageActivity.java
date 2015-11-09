package com.asap.messenger;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shaanu on 10/21/2015.
 */
public class CreateMessageActivity extends SendMessageActivity {
    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createmessage);

        final MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> draftMessagesList = appState.getDraftsList();

        final EditText newMessageText = (EditText)findViewById(R.id.newMessage);
        EditText contactNameText = (EditText)findViewById(R.id.senderContact);
        if(draftMessagesList!=null){
            for(Message draftMsg : draftMessagesList){
                newMessageText.setText(draftMsg.getMessageContent());
                contactNameText.setText(draftMsg.getMessageAddress());
                newMessageText.setTextColor(Color.BLACK);
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

                MessengerApplication appState = ((MessengerApplication) getApplicationContext());

                EditText senderContactText = (EditText) findViewById(R.id.senderContact);
                receiverContact = senderContactText.getText().toString();

                boolean isContactNumNumeric = MessageHelper.isNumeric(receiverContact);
                if(!isContactNumNumeric){
                    HashMap<String, String> phoneContacts = appState.getPhoneContacts();
                    for(String key : phoneContacts.keySet()){
                        String value = phoneContacts.get(key);
                        if(receiverContact.contentEquals(value)){
                            receiverContact = key;
                            break;
                        }
                    }
                }

                EditText newMessageText = (EditText) findViewById(R.id.newMessage);
                message = newMessageText.getText().toString();

                int hasSmsPermission = checkSelfPermission(Manifest.permission.SEND_SMS);

                if (hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSION);
                } else {
                    sendSms(receiverContact, message);
                }


                appState.setDraftsList(null);

                Intent setIntent = new Intent();
                setIntent.setClassName("com.asap.messenger", "com.asap.messenger.ViewAllMessagesActivity");
                startActivity(setIntent);
            }
        });

        contactNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                EditText senderContactText = (EditText)findViewById(R.id.senderContact);
                String senderContact = senderContactText.getText().toString();
                Toast.makeText(getBaseContext(), "Phone Number field changed "+senderContact, Toast.LENGTH_SHORT).show();
                HashMap<String, String> phoneContacts = appState.getPhoneContacts();
                if(phoneContacts.containsKey(senderContact)){
                    String nameFromPhoneBook = phoneContacts.get(senderContact);
                    senderContactText.setText(nameFromPhoneBook);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        EditText newMessageText = (EditText)findViewById(R.id.newMessage);
        String newMessage = newMessageText.getText().toString();
        System.out.println("on Back button pressed :" +newMessage);

        EditText newContactText = (EditText)findViewById(R.id.senderContact);
        String newContact = newContactText.getText().toString();

        if((newMessage!=null && !newMessage.equalsIgnoreCase(""))||(newContact!=null && !newContact.equalsIgnoreCase(""))){
            ContentValues values = new ContentValues();
            values.put("address", newContact);
            values.put("body", newMessage);
            values.put("date", String.valueOf(System.currentTimeMillis()));
            values.put("type", "3");
            values.put("thread_id", "0");
            Uri rowsInsertedURI = getContentResolver().insert(Uri.parse("content://sms/draft"), values);
            System.out.println("Inserted URI..."+rowsInsertedURI.getPath());

            MessengerApplication appState = ((MessengerApplication)getApplicationContext());
            List<Message> draftsMessageList = appState.getDraftsList();
            draftsMessageList.add(new Message(draftsMessageList.size()+1, newMessage, newContact, new Date().getTime(), MessageStatus.NEW));
            appState.setDraftsList(draftsMessageList);

            Toast.makeText(this, "Message saved as draft", Toast.LENGTH_SHORT).show();
        }

        NavUtils.navigateUpFromSameTask(this);
    }
}