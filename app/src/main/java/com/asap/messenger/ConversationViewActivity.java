package com.asap.messenger;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.custom.ConversationListAdapter;
import com.asap.messenger.custom.ViewMessagesListAdapter;
import com.asap.messenger.helper.MessageHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Intent;

/**
 * The ConversationViewActivity is a subclass of Android AppCompatActivity class
 * This class is related to the Conversation view screen
 * It takes care for creating a window for the conversation screen when the contact is selected.
 * It performs some activities related to conversation screen like displaying messages, providing context menu for each message etc.
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/24/2015
 */
public class ConversationViewActivity extends SendMessageActivity {

    MessageHelper messageHelper = new MessageHelper();
    private int[] ids;
    private String[] messages;
    String selectedContact = null;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    List<Message> originalMessageList = new ArrayList<Message>();

    /**
     * Called when the activity is started. This method has all the initialization code
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        // Getting the list of messages from the appState
        // Getting the Phone contact details from the Stock Contacts App
        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        originalMessageList = appState.getMessageList();
        HashMap<String, String> phoneContacts = appState.getPhoneContacts();
        System.out.println(originalMessageList);

        selectedContact = getIntent().getExtras().getString("selectedContact");
        List<Message> messageList = messageHelper.getMessagesByContact(selectedContact, originalMessageList);

        Iterator<Message> messageIterator = messageList.iterator();

        // Iterate over the Inbox messages and check if there is any draft message. If draft message is there, set it to the message content field
        while(messageIterator.hasNext()){
            Message message = messageIterator.next();
            System.out.println("In converstation view.. .message is.."+message.getStatus());
            if(message.getStatus().equals(MessageStatus.DRAFT)){
                EditText newMessageText = (EditText)findViewById(R.id.EditText);
                newMessageText.setText(message.getMessageContent());
                messageIterator.remove();
            }
        }

        // Set the title of the screen to the phone number. If phone number is stored in the Contacts App, then title would be contact name
        if(phoneContacts.containsKey(selectedContact)){
            setTitle(phoneContacts.get(selectedContact));
        }else{
            setTitle(selectedContact);
        }


        final String contacts[] = new String[messageList.size()];
        messages = new String[messageList.size()];
        String senders[] = new String[messageList.size()];
        String timestamps[] = new String[messageList.size()];

        ids = new int[messageList.size()];

        for(int i=0; i<messageList.size(); i++){
            messages[i] = messageList.get(i).getMessageContent();
            ids[i] = messageList.get(i).getMessageId();
        }

        // Initializing the Adapter for displaying the list of messages in the conversation screen.
        ConversationListAdapter adapter=new ConversationListAdapter(this, messageList, selectedContact);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerForContextMenu(list);

        /*
        On Click Activity for the Send Button. When it is clicked, the message has to be sent.
        It checks if the user has permissions to send the SMS. If not request for the permissions
        Once SMS is sent call the View All Messages activity.
         */
        //
        Button sendButton = (Button) findViewById(R.id.Button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiverContact = selectedContact;

                EditText newMessageText = (EditText) findViewById(R.id.EditText);
                message = newMessageText.getText().toString();

                int hasSmsPermission = checkSelfPermission(Manifest.permission.SEND_SMS);

                if (hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSION);
                } else {
                    sendSms(receiverContact, message);
                }
                //saveMessageSent(message, receiverContact);
                Intent setIntent = new Intent();
                setIntent.setClassName("com.asap.messenger", "com.asap.messenger.ViewAllMessagesActivity");
                startActivity(setIntent);
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
        getMenuInflater().inflate(R.menu.menu_conversation_view, menu);
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

    public void sendMessage(View view){
        Toast.makeText(this, "Send Message", Toast.LENGTH_LONG).show();
    }

    /**
     * Called when a context menu for the view is about to be shown.
     * @param menu The context menu that is being built
     * @param v The view for which the context menu is being built
     * @param menuInfo Extra information about the item for which the context menu should be shown. This information will vary depending on the class of v.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        System.out.println("IN Create Context menu." + ids);
        if (v.getId() == R.id.list) {
            ListView lv = (ListView) v;
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Message Actions");
            menu.add(0, v.getId(), 0, "Copy");
            menu.add(0, v.getId(), 0, "Lock");
            menu.add(0, v.getId(), 0, "Forward");
            menu.add(0, v.getId(), 0, "Delete");
        }
    }

    /**
     * This hook is called whenever an item in a context menu is selected.
     * @param item The context menu item that was selected.
     * @return Return false to allow normal context menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int messageId = ids[acmi.position];
        String message = messages[acmi.position];
        if(item.getTitle()=="Lock") {
            lockMessage(messageId);
        }
        else if(item.getTitle()=="Forward"){
            forwardMessage(message);
        }
        else if(item.getTitle()=="Delete") {
            deleteMessage(messageId);
        }else if (item.getTitle()=="Copy"){
            System.out.println("Copy message called");
            myClip = ClipData.newPlainText("copiedMessage", message);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(getApplicationContext(), "Text Copied",
                    Toast.LENGTH_SHORT).show();
        }else{
            return false;
        }
        return true;
    }

    /**
     * Method to lock the message for a given message Id
     * @param id Message Id to be locked.
     */
    public void lockMessage(int id){

        System.out.println("Locking the message id " + id);

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try{

            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("_id"))==(id)) {
                    ContentValues values = new ContentValues();
                    values.put("locked", true);
                    int rowsUpdated = getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + id, null);
                    System.out.println("Number of rows for locking updated...is " + rowsUpdated);
                }
            }
        }catch(Exception e)
        {
            System.out.println("Error in Updating lock: " + e.toString());
        }

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        originalMessageList = messageHelper.getMessagesByContact(selectedContact, originalMessageList);
        Iterator<Message> messageIterator = originalMessageList.iterator();
        while(messageIterator.hasNext()){
            Message message = messageIterator.next();
            if(message.getMessageId()==id){
                message.setLocked(true);
            }
        }
        appState.setMessageList(originalMessageList);
        finish();
        startActivity(getIntent());
    }

    /**
     * Method to forward the message
     * @param messageToForward Message id to be forwarded
     */
    public void forwardMessage(String messageToForward){
        Intent intent = new Intent("com.asap.messenger.createmessage");
        intent.putExtra("messageToForward", messageToForward);
        startActivity(intent);
    }

    /**
     * Method to delete the message
     * @param messageToDelete Message id to be deleted
     */
    public void deleteMessage(int messageToDelete){

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        boolean locked = messageHelper.checkIfMessageIsLocked(messageToDelete, originalMessageList);
        if(locked){
            openAlert(messageToDelete, "Delete Locked Message ?");
        }else{
            openAlert(messageToDelete, "Delete Message ?");
            /*Intent intent = new Intent("com.asap.messenger.deletemessage");
            System.out.println("In Delete message.. trying to delete message with id.."+messageToDelete+" for contact "+selectedContact);
            intent.putExtra("messageToDelete", messageToDelete);
            intent.putExtra("selectedContact", selectedContact);
            startActivity(intent);*/
        }


    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {

        // Check whether the user has entered any message in the message field. If yes, then save it as a draft message.
        EditText newMessageText = (EditText)findViewById(R.id.EditText);
        String newMessage = newMessageText.getText().toString();

        System.out.println("on Back button pressed :" + newMessage);

        if(newMessage!=null && !newMessage.contentEquals("")){
            MessengerApplication appState = ((MessengerApplication)getApplicationContext());
            List<Message> originalMessageList = appState.getMessageList();
            Iterator<Message> messageIterator = originalMessageList.iterator();
            while(messageIterator.hasNext()){
                Message message = messageIterator.next();
                if(message.getStatus().equals(MessageStatus.DRAFT)){
                    messageIterator.remove();
                }
            }
            appState.setMessageList(originalMessageList);
        }
        NavUtils.navigateUpFromSameTask(this);
    }


    /*
    Method to open the alert message dialog box
     */
    private void openAlert(final int messageToDelete, String messageDisplay) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConversationViewActivity.this);

        alertDialogBuilder.setTitle(messageDisplay);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent("com.asap.messenger.deletemessage");
                System.out.println("In Delete message.. trying to delete message with id.."+id+" for contact "+selectedContact);
                intent.putExtra("messageToDelete", messageToDelete);
                intent.putExtra("selectedContact", selectedContact);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
