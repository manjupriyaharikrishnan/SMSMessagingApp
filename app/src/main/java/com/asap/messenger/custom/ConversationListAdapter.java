package com.asap.messenger.custom;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asap.messenger.R;
import com.asap.messenger.helper.MessageHelper;

/**
 * Created by Umadevi on 10/18/2015.
 */
public class ConversationListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] contacts;
    private final String[] messages;
    private final String[] senders;
    private final String[] timestamps;
    private final String selectedContact;

    public ConversationListAdapter(Activity context, String[] contacts, String[] messages, String[] senders, String[] timestamps, String selectedContact) {
        super(context, R.layout.allmessageslayout, contacts);
        this.context=context;
        this.contacts=contacts;
        this.messages=messages;
        this.senders=senders;
        this.selectedContact=selectedContact;
        this.timestamps=timestamps;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.conversationview, null, true);

        LinearLayout messageLayout = (LinearLayout) rowView.findViewById(R.id.layoutMessage);

        if(selectedContact.contentEquals(senders[position])){
            messageLayout.setGravity(Gravity.LEFT);
        }else{
            messageLayout.setGravity(Gravity.RIGHT);
        }

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.usericon);

        TextView messageContent = (TextView) rowView.findViewById(R.id.message);
        messageContent.setText(messages[position]);

        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        String timeStampDisplay = MessageHelper.getDateForDisplay(timestamps[position]);
        timestamp.setText(timeStampDisplay);

        return rowView;

    };
}
