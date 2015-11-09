package com.asap.messenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import java.util.Iterator;
import java.util.List;

public class DeleteMessageActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSION = 007;

    MessageHelper messageHelper = new MessageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedContact = getIntent().getExtras().getString("selectedContact");
        int messageToDelete = getIntent().getExtras().getInt("messageToDelete");

        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();

        int hasSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);

        if(hasSmsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSION);
        }

        deleteMessageById(messageToDelete);
        Toast.makeText(this, "Message deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("com.asap.messenger.conversationview");
        intent.putExtra("selectedContact", selectedContact);
        startActivity(intent);
    }

    private void deleteMessageById(int messageToDelete){
        System.out.println("In Delete message activity..."+messageToDelete);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
        while (cursor.moveToNext())
        {
            try
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                if (messageToDelete==id)
                {
                    System.out.println("Message deleting with id "+messageToDelete);
                    String uri = "content://sms/" + messageToDelete;
                    getContentResolver().delete(Uri.parse(uri), null, null);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Integer> deletedMessagesList = appState.getDeletedMessagesList();
        deletedMessagesList.add(messageToDelete);
        appState.setDeletedMessagesList(deletedMessagesList);

        List<Message> messageList = null;
        if(messageList==null){
            Cursor inboxCursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
            messageList = messageHelper.getMessagesFromInbox(messageList, inboxCursor);
            Iterator<Message> messageIterator = messageList.iterator();
            while(messageIterator.hasNext()){
                Message message = messageIterator.next();
                if(deletedMessagesList.contains(message.getMessageId())){
                    messageIterator.remove();
                }
            }
            appState.setMessageList(messageList);
        }
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
