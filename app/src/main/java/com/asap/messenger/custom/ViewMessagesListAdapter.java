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
import com.asap.messenger.helper.MessageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewMessagesListAdapter Class is used as an Adapter class for the messages which is used in All Messages view for displaying individual messages.
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/17/2015
 */
public class ViewMessagesListAdapter extends ArrayAdapter<Message> implements Filterable {

    private final Activity context;
    private List<Message> conversationMessages;
    private List<Message> filteredConvMessages;
    private List<Message> allMessages;
    private SearchFilter searchFilter;

    /**
     * Constructor for the ConversationListAdapter for setting the initial values
     * @param context Current Context of the Activity
     * @param conversationMessages The list of the conversation messages for the selected contact
     * @param allMessages The total messages from the SMS Inbox
     */
    public ViewMessagesListAdapter(Activity context, List<Message> conversationMessages, List<Message> allMessages) {
        super(context, R.layout.allmessageslayout, conversationMessages);
        this.context=context;

        this.conversationMessages = conversationMessages;
        this.filteredConvMessages = conversationMessages;
        this.allMessages = allMessages;

        getFilter();
    }

    /**
     * Method to get the count of the messages
     * @return count of messages
     */
    @Override
    public int getCount() {
        if(filteredConvMessages!=null){
            return filteredConvMessages.size();
        }
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set
     * @param position The specific position in the list of messages
     * @param view The View of the each individual message
     * @param parent The Parent of the View
     * @return View The result View
     */
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmessageslayout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.contactName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.message);
        TextView timeStamp = (TextView) rowView.findViewById(R.id.timeStamp);

        Message rowMessage = filteredConvMessages.get(position);
        String contactNumber = rowMessage.getMessageAddress();

        if(rowMessage.getContactName()!=null && !rowMessage.getContactName().contentEquals("")){
            txtTitle.setText(rowMessage.getContactName());
        }else{
            txtTitle.setText(contactNumber);
        }
        timeStamp.setText(MessageHelper.getDateForDisplay(rowMessage.getTimestamp()));
        imageView.setImageResource(R.drawable.usericon);
        String stripString = rowMessage.getMessageContent().substring(0, Math.min(rowMessage.getMessageContent().length(), 32));
        extratxt.setText(stripString);
        return rowView;

    }

    /**
     * Method to get the filter
     * @return Filter
     */
    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter();
        }

        return searchFilter;
    }

    /**
     * Private class used for Filtering the messages
     */
    private class SearchFilter extends Filter {

        /**
         * Method to perform the filtering based on the input character sequence
         * @param constraint - parameter to do the filter accordingly
         * @return FilterResults
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Message> filteredList = new ArrayList<Message>();

            for(Message message : allMessages){
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
