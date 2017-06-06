package se.rooter.chapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * ArrayAdapter for the chat channels, extending ArrayAdapter
 */

public class ChannelAdapter extends ArrayAdapter<ChannelMessage> {

    private static final String TAG = "ChannelAdapter";

    private TextView userName;
    private TextView message;
    private ImageView avatarRound;

    private final int radius = 5;
    private final int margin = 5;
    private final Transformation transformation = new RoundedCornersTransformation(radius, margin);

    public ChannelAdapter(Context context, ArrayList<ChannelMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /* Get ChannelMessage object from list */
        final ChannelMessage channelMessage = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_message_item, parent, false);
        }

        /* Get views*/
        userName = (TextView) convertView.findViewById(R.id.userNickname);
        message = (TextView) convertView.findViewById(R.id.userMessage);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);

        /* Get imgPath*/
        String imgPath = channelMessage.getImgPath();

        /* Place image from imgpath in view */
        Picasso.with(getContext()).load(imgPath).resize(50, 50).placeholder(R.drawable.ic_action_name).into(avatarRound);

        /* Set name and message */
        userName.setText(channelMessage.getSenderName());
        message.setText(channelMessage.getMessage());

        return convertView;
    }

}
