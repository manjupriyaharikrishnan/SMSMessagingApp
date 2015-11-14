package com.asap.messenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
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

/**
 * The SendMessageActivity is a subclass of Android AppCompatActivity class
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/24/2015
 */
public class SendMessageActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSION = 007;
    String receiverContact;
    String message;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    public BroadcastReceiver sendBroadcastReceiver;
    public BroadcastReceiver deliveryBroadcastReciever;

    /**
     * Method to send the SMS to the receiver
     * @param contact The Contact name or number to whom message is intended to deliver - might contain multiple contain multiple contacts
     * @param message The message to be delivered
     */
    public void sendSms(String contact, String message){
        if(contact.contains(",")){
            String indContacts[] = contact.split(",");
            for(String eachContact : indContacts){
                sendSmsIndividual(eachContact, message);
            }
        }else{
            sendSmsIndividual(contact, message);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 20000);
    }

    /**
     * Method to send the SMS to the receiver
     * @param contact The Contact name or number to whom message is intended to deliver
     * @param message The message to be delivered
     */
    public void sendSmsIndividual(String contact, String message){
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // —when the SMS has been sent—
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // —when the SMS has been delivered—
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact, null, message, sentPI, deliveredPI);
        System.out.println("Message Sent Successfully");
    }

    /**
     * Callback for the result from requesting permissions.
     * @param requestCode The request code passed in requestPermissions(String[], int).
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
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
