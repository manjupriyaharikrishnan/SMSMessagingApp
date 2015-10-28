package com.asap.messenger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SendMessageActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context,"SMS Sent", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context,"SMS Sending Failed", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
