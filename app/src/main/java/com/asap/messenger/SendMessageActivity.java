package com.asap.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SendMessageActivity extends AppCompatActivity {

    protected String senderContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        senderContact = getIntent().getExtras().getString("senderContact");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmessagelayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
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

    public boolean onOkClicked(View view)
    {
        //String selectedContact = "222-222-2222";
        Intent intent = new Intent("com.asap.messenger.conversationview");
        intent.putExtra("selectedContact", senderContact);
        startActivity(intent);
        return true;
    }
}
