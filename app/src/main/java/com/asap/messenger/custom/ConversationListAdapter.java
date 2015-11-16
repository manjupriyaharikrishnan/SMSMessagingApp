package com.asap.messenger.custom;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asap.messenger.R;
import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import java.util.List;

/**
 * The ConversationListAdapter Class is used as an Adapter class for the messages which is used in Conversation view for displaying individual messages.
 * @author  Umadevi Samudrala
 * @version 1.0
 * @since 10/18/2015
 */
public class ConversationListAdapter extends ArrayAdapter<Message> {

    private final Activity context;
    List<Message> messageList;
    private List<Integer> lockedMessagesList;
    private final String selectedContact;

    /**
     * Constructor for the ConversationListAdapter for setting the initial values
     * @param context Current Context of the Activity
     * @param messageList The list of the messages on which activity has to be performed
     * @param selectedContact The Contact selected by the user from the View All Messages Screen
     */
    public ConversationListAdapter(Activity context, List<Message> messageList, String selectedContact, List<Integer> lockedMessagesList) {
        super(context, R.layout.allmessageslayout, messageList);
        this.context=context;
        this.selectedContact=selectedContact;
        this.messageList = messageList;
        this.lockedMessagesList = lockedMessagesList;
    }

    /**
     * Get a View that displays the data at the specified position in the data set
     * @param position The specific position in the list of messages
     * @param view The View of the each individual message
     * @param parent The Parent of the View
     * @return View The result View
     */
    public View getView(int position,View view,ViewGroup parent) {

        // For each message that is displayed get the message by using the position
        Message currentMessage = messageList.get(position);
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.conversationview, null, true);

        LinearLayout messageLayout = (LinearLayout) rowView.findViewById(R.id.layoutMessage);
        LinearLayout timeStampLockLayout = (LinearLayout) rowView.findViewById(R.id.timeStampLock);

        /*If the message is received then align to the left
            if the message is sent then align to the right
         */

        if(currentMessage.getStatus().contentEquals(MessageStatus.RECEIVED)){
            messageLayout.setGravity(Gravity.LEFT);
            timeStampLockLayout.setGravity(Gravity.LEFT);
        }else if(currentMessage.getStatus().contentEquals(MessageStatus.SENT)){
            messageLayout.setGravity(Gravity.RIGHT);
            timeStampLockLayout.setGravity(Gravity.RIGHT);
        }

        // Setting the values and images
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.usericon);

        TextView messageContent = (TextView) rowView.findViewById(R.id.message);
        messageContent.setText(currentMessage.getMessageContent());

        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        String timeStampDisplay = MessageHelper.getDateForDisplay(currentMessage.getTimestamp());
        timestamp.setText(timeStampDisplay);

        if(lockedMessagesList.contains(currentMessage.getMessageId())){
            ImageView lockView = (ImageView) rowView.findViewById(R.id.lock);
            lockView.setImageResource(R.drawable.lock);
        }
        return rowView;

    }
}
