package com.asap.messenger.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asap.messenger.R;

/**
 * Created by Umadevi on 10/17/2015.
 */
public class ViewMessagesListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] contacts;
    private final String[] messages;

    public ViewMessagesListAdapter(Activity context, String[] contacts, String[] messages) {
        super(context, R.layout.allmessageslayout, contacts);
        this.context=context;
        this.contacts=contacts;
        this.messages=messages;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmessageslayout, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.contactName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.message);

        txtTitle.setText(contacts[position]);
        imageView.setImageResource(R.drawable.usericon);
        extratxt.setText(messages[position]);
        return rowView;

    };
}
