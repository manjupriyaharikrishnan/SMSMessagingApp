package com.asap.messenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;

import java.util.List;

public class SendMessageActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSION = 007;
    String receiverContact;
    String message;

    public void saveMessageSent(String sentMessage, String receiverContact){
        MessengerApplication appState = ((MessengerApplication)getApplicationContext());
        List<Message> originalMessageList = appState.getMessageList();
        originalMessageList.add(new Message(52, sentMessage, "111-111-1111", receiverContact, "10-17-2015 12:23:22", MessageStatus.SENT));
        appState.setMessageList(originalMessageList);
    }

    public void sendSms(String contact, String message){
        Intent sendIntent = new Intent(this, SendMessageActivity.class);
        PendingIntent sendPI = PendingIntent.getBroadcast(this.getApplicationContext(), 0, sendIntent, 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact, null, message, sendPI, null);
        System.out.println("Message Sent Successfully");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms(receiverContact, message);
                } else {
                    Toast.makeText(this, "Send Message Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
