package com.asap.messenger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

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

        final EditText newMessage = (EditText)findViewById(R.id.newMessage);


        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        System.out.println(originalMessageList);


        for(Message message : originalMessageList){
            if(message.getStatus().equals(MessageStatus.NEW)){
                EditText newMessageText = (EditText)findViewById(R.id.newMessage);
                newMessageText.setText(message.getMessageContent());
                originalMessageList.remove(message);
            }
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String messageToForward = getIntent().getExtras().getString("messageToForward");
            if(messageToForward!=null){
                newMessage.setText(messageToForward);
                newMessage.setTextColor(Color.BLACK);
            }else{
                newMessage.setHint("Type Message");
                newMessage.setTextColor(Color.GRAY);
            }
        }else{
            newMessage.setHint("Type Message");
            newMessage.setTextColor(Color.GRAY);
        }

        newMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                newMessage.setText("");
                newMessage.setTextColor(Color.BLACK);
                return false;
            }
        });
    }

    public void sendMessage(View view){
        EditText newMessageText = (EditText)findViewById(R.id.newMessage);
        String newMessage = newMessageText.getText().toString();

        EditText senderContactText = (EditText)findViewById(R.id.senderContact);
        String senderContact = senderContactText.getText().toString();

        System.out.println("New Message is "+newMessage);
        System.out.println("New Message Contact is " + senderContact);
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
        originalMessageList.add(new Message(52, newMessage, newContact , "111-111-1111", "10-17-2015", MessageStatus.NEW));
        startActivity(setIntent);
    }
}