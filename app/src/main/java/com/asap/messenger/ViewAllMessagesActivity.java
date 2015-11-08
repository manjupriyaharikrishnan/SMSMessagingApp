package com.asap.messenger;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.net.Uri;
import android.database.Cursor;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.custom.ViewMessagesListAdapter;
import com.asap.messenger.helper.MessageHelper;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ViewAllMessagesActivity extends ContactManagerActivity implements SearchView.OnQueryTextListener{

    ListView list;
    MessageHelper messageHelper = new MessageHelper();
    final int REQUEST_CODE_ASK_PERMISSION = 007;

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ViewMessagesListAdapter adapter;
    HashMap<String, String> phoneContacts = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In ViewAllMessagesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int hasSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);

        if(hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSION);
        }

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> messageList = null;
        if(messageList==null){
            Cursor inboxCursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
            messageList = messageHelper.getMessagesFromInbox(messageList, inboxCursor);
            appState.setMessageList(messageList);
        }



        int hasContactPermissions = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if(hasContactPermissions != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permissions are not there");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSION_FOR_READ);
            System.out.println("Permissions requested");
        } else {
            phoneContacts = fetchContacts();
        }
        appState.setPhoneContacts(phoneContacts);


        final List<Message> sortedList = messageHelper.getLatestMessagesByAllContacts(messageList, phoneContacts);
        adapter=new ViewMessagesListAdapter(this, sortedList);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Message selectedMessage = sortedList.get(position);
                String selectedContact = selectedMessage.getMessageAddress();
                /*if(MessageStatus.RECEIVED.contentEquals(selectedMessage.getStatus())){
                    selectedContact = selectedMessage.getSender().getPhoneNumber();
                }else if(MessageStatus.SENT.contentEquals(selectedMessage.getStatus())){
                    selectedContact = selectedMessage.getReceiver().get(0).getPhoneNumber();
                }*/
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
