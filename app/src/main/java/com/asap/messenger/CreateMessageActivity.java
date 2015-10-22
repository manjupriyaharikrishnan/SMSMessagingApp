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

        Message currentmessage=new Message();

        final EditText editText = (EditText)findViewById(R.id.EditText);
        editText.setHint("Type Message");
        editText.setTextColor(Color.GRAY);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setText("");
                return false;
            }
        });


    }

    }