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
import com.asap.messenger.bo.Message;
import com.asap.messenger.common.MessageStatus;
import com.asap.messenger.helper.MessageHelper;

import java.util.List;

/**
 * Created by Umadevi on 10/18/2015.
 */
public class ConversationListAdapter extends ArrayAdapter<Message> {

    private final Activity context;
    List<Message> messageList;
    private final String selectedContact;

    public ConversationListAdapter(Activity context, List<Message> messageList, String selectedContact) {
        super(context, R.layout.allmessageslayout, messageList);
        this.context=context;
        this.selectedContact=selectedContact;
        this.messageList = messageList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        Message currentMessage = messageList.get(position);
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.conversationview, null, true);

        LinearLayout messageLayout = (LinearLayout) rowView.findViewById(R.id.layoutMessage);

        if(selectedContact.contentEquals(currentMessage.getSender().getPhoneNumber())){
            messageLayout.setGravity(Gravity.LEFT);
        }else{
            messageLayout.setGravity(Gravity.RIGHT);
        }

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.usericon);

        TextView messageContent = (TextView) rowView.findViewById(R.id.message);
        messageContent.setText(currentMessage.getMessageContent());

        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        String timeStampDisplay = MessageHelper.getDateForDisplay(currentMessage.getTimestamp());
        timestamp.setText(timeStampDisplay);

        if(currentMessage.getStatus().equals(MessageStatus.LOCK)){
            ImageView lockView = (ImageView) rowView.findViewById(R.id.lock);
            lockView.setImageResource(R.drawable.lock);
        }
        return rowView;

    };
}
