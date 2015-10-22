package com.asap.messenger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.asap.messenger.helper.MessageHelper;

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
        newMessage.setHint("Type Message");
        newMessage.setTextColor(Color.GRAY);

        newMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                newMessage.setText("");
                newMessage.setTextColor(Color.BLACK);
                return false;
            }
        });
    }
}