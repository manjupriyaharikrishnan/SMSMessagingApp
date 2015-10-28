package com.asap.messenger;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
    final int REQUEST_CODE_ASK_PERMISSION = 007;
    String senderContact, message;

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

                EditText senderContactText = (EditText)findViewById(R.id.senderContact);
                senderContact = senderContactText.getText().toString();

                EditText newMessageText = (EditText)findViewById(R.id.newMessage);
                message = newMessageText.getText().toString();

                int hasSmsPermission = checkSelfPermission(Manifest.permission.SEND_SMS);

                if(hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSION);
                } else {
                    sendSms(senderContact, message);
                }
            }
        });
    }

    public void sendSms(String contact, String message){
        Intent sendIntent = new Intent(this, SendMessageActivity.class);
        PendingIntent sendPI = PendingIntent.getBroadcast(this.getApplicationContext(), 0, sendIntent, 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(senderContact, null, message, sendPI, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms(senderContact, message);
                } else {
                    Toast.makeText(this, "Send Message Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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