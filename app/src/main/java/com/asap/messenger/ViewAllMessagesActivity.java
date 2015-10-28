package com.asap.messenger;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
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


public class ViewAllMessagesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView list;
    MessageHelper messageHelper = new MessageHelper();

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ViewMessagesListAdapter adapter;

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

        List<Message> sortedList = messageHelper.getLatestMessagesByAllContacts(messageList);
        final String contacts[] = new String[sortedList.size()];
        String messages[] = new String[sortedList.size()];
        for(int i=0; i<sortedList.size(); i++){
            contacts[i] = sortedList.get(i).getSender().getContactName();
            messages[i] = sortedList.get(i).getMessageContent();
        }

        adapter=new ViewMessagesListAdapter(this, contacts, messages);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
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
        Intent intent = new Intent("com.asap.messenger.createmessage");
        startActivity(intent);
    }

    public void searchMessages(MenuItem item){
        Intent intent = new Intent("com.asap.messenger.searchmessage");

        startActivity(intent);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

}
