package com.asap.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                System.out.println("Long clicked.."+pos);
                return true;
            }
        });
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
}
