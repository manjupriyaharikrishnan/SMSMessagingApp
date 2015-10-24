package com.asap.messenger.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.asap.messenger.R;
import com.asap.messenger.bo.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umadevi on 10/17/2015.
 */
public class ViewMessagesListAdapter extends ArrayAdapter<String> implements Filterable {

    private final Activity context;
    private String[] contacts;
    private String[] messages;

    private SearchFilter searchFilter;

    private String[] filteredContacts;
    private String[] filteredMessages;

    public ViewMessagesListAdapter(Activity context, String[] contacts, String[] messages) {
        super(context, R.layout.allmessageslayout, contacts);
        this.context=context;
        this.contacts=contacts;
        this.messages=messages;

        this.filteredContacts = contacts;
        this.filteredMessages=messages;

        getFilter();
    }

    @Override
    public int getCount() {
        if(filteredContacts!=null){
            return filteredContacts.length;
        }
        return 0;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmessageslayout, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.contactName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.message);

        txtTitle.setText(filteredContacts[position]);
        imageView.setImageResource(R.drawable.usericon);
        extratxt.setText(filteredMessages[position]);
        return rowView;

    };

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter();
        }

        return searchFilter;
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Message> filteredList = new ArrayList<Message>();

            for(int i=0; i<messages.length; i++){
                if(messages[i].toLowerCase().contains(filterString)){
                    Message message = new Message(0,messages[i], contacts[i], contacts[i], null);
                    filteredList.add(message);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            List<Message> filteredMessageList = (ArrayList<Message>) results.values;
            filteredContacts = new String[filteredMessageList.size()];
            filteredMessages = new String[filteredMessageList.size()];
            for(int i=0; i<filteredMessageList.size(); i++){
                filteredContacts[i] = filteredMessageList.get(i).getReceiver().get(0).getPhoneNumber();
                filteredMessages[i] = filteredMessageList.get(i).getMessageContent();
                System.out.println("In PUshm...."+filteredMessages[i]);
            }
            /*contacts = filteredContacts;
            messages = filteredMessages;*/
            notifyDataSetChanged();
        }
    }
}
