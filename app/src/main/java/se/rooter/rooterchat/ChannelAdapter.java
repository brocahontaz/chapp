package se.rooter.rooterchat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChannelAdapter extends ArrayAdapter<ChannelMessage> {

    public ChannelAdapter(Context context, ArrayList<ChannelMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ChannelMessage channelMessage = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_message_item, parent, false);
        }

        TextView userName = (TextView) convertView.findViewById(R.id.userNickname);
        TextView message = (TextView) convertView.findViewById(R.id.userMessage);
        ImageView userAvatar = (ImageView) convertView.findViewById(R.id.userAvatar);

        userName.setText(channelMessage.getSenderID());
        message.setText(channelMessage.getMessage());


        return convertView;
    }
}
