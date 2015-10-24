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
import android.content.Intent;

public class ConversationViewActivity extends AppCompatActivity {

    MessageHelper messageHelper = new MessageHelper();
    private int[] ids;
    String selectedContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        System.out.println(originalMessageList);

        selectedContact = getIntent().getExtras().getString("selectedContact");
        List<Message> messageList = messageHelper.getMessagesByContact(selectedContact, originalMessageList);

        setTitle(selectedContact);

        final String contacts[] = new String[messageList.size()];
        String messages[] = new String[messageList.size()];
        String senders[] = new String[messageList.size()];
        String timestamps[] = new String[messageList.size()];

        ids = new int[messageList.size()];

        for(int i=0; i<messageList.size(); i++){
            contacts[i] = messageList.get(i).getSender().getContactName();
            messages[i] = messageList.get(i).getMessageContent();
            senders[i] = messageList.get(i).getSender().getPhoneNumber();
            timestamps[i] = messageList.get(i).getTimestamp();
            ids[i] = messageList.get(i).getMessageId();
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
        System.out.println("IN Create Context menu."+ids);
        if (v.getId() == R.id.list) {
            ListView lv = (ListView) v;
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Message Actions");
            menu.add(0, v.getId(), 0, "Reply");
            menu.add(0, v.getId(), 0, "Forward");
            menu.add(0, v.getId(), 0, "Delete");

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int messageId = ids[acmi.position];
        System.out.println("IN onContextItemSelected."+ids[acmi.position]);
        if(item.getTitle()=="Reply") {
            replyMessage(item.getItemId());
        }
        else if(item.getTitle()=="Forward"){
            forwardMessage(item.getItemId());
        }
        else if(item.getTitle()=="Delete") {
            deleteMessage(messageId);
        }else{
            return false;
        }
        return true;
    }

    public void replyMessage(int id){
        Toast.makeText(this, "replyMessage called", Toast.LENGTH_SHORT).show();
    }
    public void forwardMessage(int id){
       // Toast.makeText(this, "forwardMessage called", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("com.asap.messenger.forwardmessage");
        startActivity(intent);
    }
    public void deleteMessage(int id){
        Intent intent = new Intent("com.asap.messenger.deletemessage");
        System.out.println("In Delete message.. trying to delete message with id.."+id+" for contact "+selectedContact);
        intent.putExtra("messageToDelete", id);
        intent.putExtra("selectedContact", selectedContact);
        startActivity(intent);
    }
}
