package com.asap.messenger;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import android.database.Cursor;

import com.asap.messenger.bo.Message;
import com.asap.messenger.custom.ViewMessagesListAdapter;
import com.asap.messenger.helper.MessageHelper;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

/**
 * The ViewAllMessagesActivity is a subclass of Android AppCompatActivity class
 * This class is related to the View All Messages screen
 * It takes care for creating a window for the View All Messages screen when the App loads initially.
 * It performs some activities related to View All Messages screen like displaying messages, providing context menu for each message etc.
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/24/2015
 */
public class ViewAllMessagesActivity extends ContactManagerActivity implements SearchView.OnQueryTextListener{

    final int REQUEST_CODE_ASK_PERMISSION = 007;
    ListView list;
    MessageHelper messageHelper = new MessageHelper();
    ViewMessagesListAdapter adapter;
    HashMap<String, String> phoneContacts = new HashMap<String, String>();
    private SearchView searchView;
    private MenuItem searchMenuItem;

    /**
     * Called when the activity is started. This method has all the initialization code
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In ViewAllMessagesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user has permission to read the SMS. If not request for the permissions.
        int hasSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);

        if(hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSION);
        }

        // Get the messages from the SMS Inbox
        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> messageList = null;
        if(messageList==null){
            Cursor inboxCursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
            messageList = messageHelper.getMessagesFromInbox(messageList, inboxCursor);
            List<Integer> deletedMessagesList = appState.getDeletedMessagesList();
            Iterator<Message> messageIterator = messageList.iterator();
            while(messageIterator.hasNext()){
                Message message = messageIterator.next();
                if(deletedMessagesList.contains(message.getMessageId())){
                    messageIterator.remove();
                }
            }
            appState.setMessageList(messageList);

        }

        // Check if the user has the permissions to read the contacts from the Stock Contacts App
        int hasContactPermissions = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if(hasContactPermissions != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permissions are not there");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSION_FOR_READ);
            System.out.println("Permissions requested");
        } else {
            phoneContacts = fetchContacts();
        }
        appState.setPhoneContacts(phoneContacts);

        // Get the messages sorted according to timestamp and grouped by the contact number.
        final List<Message> sortedList = messageHelper.getLatestMessagesByAllContacts(messageList, phoneContacts);
        adapter=new ViewMessagesListAdapter(this, sortedList, messageList);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        // For each message, set on click activity to be performed. When user clicks a particular message, the conversation view has to be loaded.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Message selectedMessage = sortedList.get(position);
                String selectedContact = selectedMessage.getMessageAddress();
                Intent intent = new Intent("com.asap.messenger.conversationview");
                intent.putExtra("selectedContact", selectedContact);
                startActivity(intent);
            }
        });

    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return Return true for the menu to be displayed; if you return false it will not be shown.
     */
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

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method called when the create new message action is performed.
     * Call the create message activity.
     * @param item Menu Item
     */
    public void createNewMessage(MenuItem item){
        Intent intent = new Intent("com.asap.messenger.createmessage");
        startActivity(intent);
    }

    /**
     * Method called when the Search message action is performed.
     * Call the Search message activity.
     * @param item Menu Item
     */
    public void searchMessages(MenuItem item){
        Intent intent = new Intent("com.asap.messenger.searchmessage");

        startActivity(intent);

    }

    /**
     * Method invoked when the search query is submitted, either by dismissing the keyboard, pressing search or next on the keyboard or when voice has detected the end of the query.
     * @param query The query entered.
     * @return whether the results changed as a result of the query.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Method invoked when the search query is updated. This is called as soon as the query changes;
     * @param newText The current search query.
     * @return whether the results changed as a result of the new query.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
