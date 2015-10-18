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

/**
 * Created by Umadevi on 10/18/2015.
 */
public class ConversationListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] contacts;
    private final String[] messages;
    private final String[] senders;
    private final String selectedContact;

    public ConversationListAdapter(Activity context, String[] contacts, String[] messages, String[] senders, String selectedContact) {
        super(context, R.layout.allmessageslayout, contacts);
        this.context=context;
        this.contacts=contacts;
        this.messages=messages;
        this.senders=senders;
        this.selectedContact=selectedContact;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.conversationview, null, true);

        LinearLayout messageLayout = (LinearLayout) rowView.findViewById(R.id.layoutMessage);
        System.out.println("Contact Selected..."+selectedContact);
        System.out.println("Item Sender ..."+senders[position]);
        if(selectedContact.contentEquals(senders[position])){
            messageLayout.setGravity(Gravity.LEFT);
        }else{
            messageLayout.setGravity(Gravity.RIGHT);
        }

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.usericon);

        TextView extratxt = (TextView) rowView.findViewById(R.id.message);
        extratxt.setText(messages[position]);
        return rowView;

    };
}
