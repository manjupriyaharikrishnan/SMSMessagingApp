package com.asap.messenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The AddContactActivity is a subclass of Android AppCompatActivity class
 * This class is related to the add the contact number to stock contact app
 * It takes care for creating a window for capturing the contact details
 * It performs activity related to saving the contact details that are captured into stock contact app
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 11/12/2015
 */
public class AddContactActivity extends AppCompatActivity {

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
                String addContactNo = addContactNoText.getText().toString();

                EditText addContactNameText = (EditText) findViewById(R.id.addContactName);
                String addContactName = addContactNameText.getText().toString();

                System.out.println("Contact to save is "+addContactName +", Contact no is "+addContactNo);

                addContactToStockApp(addContactName, addContactNo);

                Toast.makeText(getApplicationContext(), "Contact Saved", Toast.LENGTH_SHORT).show();

                Intent setIntent = new Intent();
                setIntent.setClassName("com.asap.messenger", "com.asap.messenger.ConversationViewActivity");
                setIntent.putExtra("selectedContact", selectedContact);
                startActivity(setIntent);
            }
        });
    }

    private void addContactToStockApp(String addContactName, String addContactNo){
        // Write Logic to add contact details to contact app.
        // Request permissions to write. etc etc
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
