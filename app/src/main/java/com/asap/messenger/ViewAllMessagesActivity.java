package com.asap.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.custom.ViewMessagesListAdapter;
import com.asap.messenger.helper.MessageHelper;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewAllMessagesActivity extends AppCompatActivity {

    ListView list;
    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> messageList = appState.getMessageList();
        if(messageList==null){
            messageList = messageHelper.getAllMessages();
            appState.setMessageList(messageList);
        }

        final String contacts[] = new String[messageList.size()];
        String messages[] = new String[messageList.size()];
        for(int i=0; i<messageList.size(); i++){
            contacts[i] = messageList.get(i).getSender().getContactName();
            messages[i] = messageList.get(i).getMessageContent();
        }

        ViewMessagesListAdapter adapter=new ViewMessagesListAdapter(this, contacts, messages);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedContact = contacts[+position];
                Intent intent = new Intent("com.asap.messenger.conversationview");
                intent.putExtra("selectedContact", selectedContact);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNewMessage(MenuItem item){
       // Toast.makeText(this, "Create New Message", Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.asap.messenger.createmessage");
       // EditText editText=(EditText) findViewById(R.id.editText);
       // String message = editText.getText().toString();
       // intent.putExtra("currentmessage", message);
        startActivity(intent);

    }

    public void searchMessages(MenuItem item){
        //Toast.makeText(this, "Search Messages", Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.asap.messenger.searchmessage");

        startActivity(intent);

    }
}
