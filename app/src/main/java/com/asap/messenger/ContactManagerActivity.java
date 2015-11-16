package com.asap.messenger;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;

/**
 * The ContactManagerActivity is a subclass of Android AppCompatActivity class
 * It enables the users to interact with the Stock Contacts App to get the contact details stored there
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/24/2015
 */
public class ContactManagerActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSION_FOR_READ = 006;

    /**
     * Method to fetch the contact details from the Stock Contacts App to get the contact details
     * @return Map storing the Contact Details. Key is ContactNum and Value is ContactName
     */
    public HashMap<String, String> fetchContacts(){
        System.out.println("In Fetch Contacts");
        HashMap<String, String> phoneContacts = new HashMap<String, String>();
        String phoneNumber = null;

        // Inputs required for querying the Contacts App
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();

        // Querying the Stock Contacts App and getting the results in the form of Cursor
        // Proper permissions are required to read the contacts
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);


        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // For each contact getting the name and phone number and adding to the HashMap
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:" + name);
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNumber = phoneNumber.replace("-", "");
                        phoneNumber = phoneNumber.replace(" ", "");
                        output.append("\n Phone number:" + phoneNumber);
                        phoneContacts.put(phoneNumber, name);
                    }
                    phoneCursor.close();
                }output.append("\n");
            }
            System.out.println("output is "+output);
        }
        System.out.println("PHone contacts are ..."+phoneContacts);
        return phoneContacts;
    }

    /**
     * Method to request for the permissions to access the Stock Contacts App
     * @param requestCode Request code to access the Stock Contacts App - 006
     * @param permissions Set of permissions required on the Stock Contacts App
     * @param grantResults The results granted when the permission is requested.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("Got Results for permissions");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSION_FOR_READ:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchContacts();
                } else {
                    Toast.makeText(this, "Contact Manager No permissions", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
