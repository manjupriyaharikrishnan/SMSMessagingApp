package com.asap.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.custom.ConversationListAdapter;
import com.asap.messenger.custom.ViewMessagesListAdapter;
import com.asap.messenger.helper.MessageHelper;


import java.util.List;

public class ConversationViewActivity extends AppCompatActivity {

    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        System.out.println(originalMessageList);

        final String selectedContact = getIntent().getExtras().getString("selectedContact");
        List<Message> messageList = messageHelper.getMessagesByContact(selectedContact, originalMessageList);

        setTitle(selectedContact);

        final String contacts[] = new String[messageList.size()];
        String messages[] = new String[messageList.size()];
        String senders[] = new String[messageList.size()];
        String timestamps[] = new String[messageList.size()];

        for(int i=0; i<messageList.size(); i++){
            contacts[i] = messageList.get(i).getSender().getContactName();
            messages[i] = messageList.get(i).getMessageContent();
            senders[i] = messageList.get(i).getSender().getPhoneNumber();
            timestamps[i] = messageList.get(i).getTimestamp();
        }

        ConversationListAdapter adapter=new ConversationListAdapter(this, contacts, messages, senders, timestamps, selectedContact);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerForContextMenu(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation_view, menu);
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

    public void sendMessage(View view){
        Toast.makeText(this, "Send Message", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        System.out.println("IN Create Context menu.");
        if (v.getId() == R.id.list) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Message Actions");
            menu.add(0, v.getId(), 0, "Reply");
            menu.add(0, v.getId(), 0, "Forward");
            menu.add(0, v.getId(), 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Reply") {
            replyMessage(item.getItemId());
        }
        else if(item.getTitle()=="Forward"){
            forwardMessage(item.getItemId());
        }
        else if(item.getTitle()=="Delete") {
            deleteMessage(item.getItemId());
        }else{
            return false;
        }
        return true;
    }

    public void replyMessage(int id){
        Toast.makeText(this, "replyMessage called", Toast.LENGTH_SHORT).show();
    }
    public void forwardMessage(int id){
        Toast.makeText(this, "forwardMessage called", Toast.LENGTH_SHORT).show();
    }
    public void deleteMessage(int id){
        Toast.makeText(this, "deleteMessage called", Toast.LENGTH_SHORT).show();
    }
}