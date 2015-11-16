package com.asap.messenger;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The AddContactActivity is a subclass of Android AppCompatActivity class
 * This class is related to the add the contact number to stock contact app
 * It takes care for creating a window for capturing the contact details
 * It performs activity related to saving the contact details that are captured into stock contact app
 * @author  Umadevi Samudrala, Rajiv Shanmugam Madeswaran
 * @version 1.0
 * @since 11/12/2015
 */
public class AddContactActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSIONS = 005;
    String addContactName, addContactNo;

    /**
     * Called when the activity is started. This method has all the initialization code
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add to Contacts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        final String selectedContact = getIntent().getExtras().getString("contactToAdd");
        EditText addContactNoText = (EditText) findViewById(R.id.addContactNo);
        addContactNoText.setText(selectedContact);

        Button addContactButton = (Button) findViewById(R.id.addContact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addContactNoText = (EditText) findViewById(R.id.addContactNo);
                addContactNo = addContactNoText.getText().toString();

                EditText addContactNameText = (EditText) findViewById(R.id.addContactName);
                addContactName = addContactNameText.getText().toString();

                System.out.println("Contact to save is " + addContactName + ", Contact no is " + addContactNo);

                addContactToStockApp(addContactName, addContactNo);

                Toast.makeText(getApplicationContext(), "Contact Saved", Toast.LENGTH_SHORT).show();

                Intent setIntent = new Intent();
                setIntent.setClassName("com.asap.messenger", "com.asap.messenger.ConversationViewActivity");
                setIntent.putExtra("selectedContact", selectedContact);
                startActivity(setIntent);
            }
        });
    }

    /**
     * function call to write any new number in to the Stock App
     * @param addContactName Name to store for the particular contact
     * @param addContactNo Phone Number for the particular contact
     */
    public void insertContact(String contactName, String contactNumber){

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);
        int rawContactID = operations.size();

        operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, contactName)
                .build());

        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // Apply the operations.
        ContentResolver resolver = getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * function call to check whether the user has set the permissions to write any new number in to the Stock App
     * If so, it will call insertContact to insert in to Stock App else it will ask for the user permission.
     * @param addContactName Name to store for the particular contact
     * @param addContactNo Phone Number for the particular contact
     */
    private void addContactToStockApp(String addContactName, String addContactNo){
        // Write Logic to add contact details to contact app.
        // Request permissions to write. etc etc
        System.out.println("inside add");

        int hasWriteContactPermission  = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
        if(hasWriteContactPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        System.out.println("skipped permission inserting...");
        insertContact(addContactName, addContactNo);
    }

    /**
     * Callback for the result from requesting permissions.
     * @param requestCode The request code passed in requestPermissions(String[], int).
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    insertContact(addContactName, addContactNo);
                } else {
                    Toast.makeText(this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return Return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
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
