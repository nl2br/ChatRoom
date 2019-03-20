package fr.mds.chatroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.mds.chatroom.R;
import fr.mds.chatroom.model.Message;

public class MessageViewAdapter extends ArrayAdapter<Message> {

    public MessageViewAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the item in the array at the good position
        Message message = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        TextView tv_chat_room_message_item_user = convertView.findViewById(R.id.tv_chat_room_message_item_user);
        TextView tv_chat_room_message_item_content = convertView.findViewById(R.id.tv_chat_room_message_item_content);

        tv_chat_room_message_item_user.setText(message.getUser());
        tv_chat_room_message_item_content.setText(message.getContent());

        return convertView;
    }
}
