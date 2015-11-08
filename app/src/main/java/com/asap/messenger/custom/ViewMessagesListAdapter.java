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
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umadevi on 10/17/2015.
 */
public class ViewMessagesListAdapter extends ArrayAdapter<Message> implements Filterable {

    private final Activity context;

    private List<Message> conversationMessages;
    private List<Message> filteredConvMessages;

    private SearchFilter searchFilter;

    public ViewMessagesListAdapter(Activity context, List<Message> conversationMessages) {
        super(context, R.layout.allmessageslayout, conversationMessages);
        this.context=context;

        this.conversationMessages = conversationMessages;
        this.filteredConvMessages = conversationMessages;

        getFilter();
    }

    @Override
    public int getCount() {
        if(filteredConvMessages!=null){
            return filteredConvMessages.size();
        }
        return 0;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmessageslayout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.contactName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.message);
        TextView timeStamp = (TextView) rowView.findViewById(R.id.timeStamp);

        Message rowMessage = filteredConvMessages.get(position);
        String contactNumber = rowMessage.getMessageAddress();
        /*if(MessageStatus.RECEIVED.contentEquals(rowMessage.getStatus())){
            contactNumber = rowMessage.getSender().getPhoneNumber();
        }else if(MessageStatus.SENT.contentEquals(rowMessage.getStatus())){
            contactNumber = rowMessage.getReceiver().get(0).getPhoneNumber();
        }*/
        if(rowMessage.getContactName()!=null && !rowMessage.getContactName().contentEquals("")){
            txtTitle.setText(rowMessage.getContactName());
        }else{
            txtTitle.setText(contactNumber);
        }
        timeStamp.setText(MessageHelper.getDateForDisplay(rowMessage.getTimestamp()));
        imageView.setImageResource(R.drawable.usericon);
        extratxt.setText(rowMessage.getMessageContent());
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

            for(Message message : conversationMessages){
                if(message.getMessageContent().toLowerCase().contains(filterString)){
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

            filteredConvMessages = (ArrayList<Message>) results.values;
            notifyDataSetChanged();
        }
    }
}
