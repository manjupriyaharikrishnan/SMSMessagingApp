package com.asap.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.asap.messenger.bo.Message;
import com.asap.messenger.helper.MessageHelper;

import java.util.List;

public class DeleteMessageActivity extends AppCompatActivity {

    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedContact = getIntent().getExtras().getString("selectedContact");
        int messageToDelete = getIntent().getExtras().getInt("messageToDelete");

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();

        List<Message> deletedMessageList = messageHelper.deleteMessageById(messageToDelete, originalMessageList);
        appState.setMessageList(deletedMessageList);

        Intent intent = new Intent("com.asap.messenger.conversationview");
        intent.putExtra("selectedContact", selectedContact);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
