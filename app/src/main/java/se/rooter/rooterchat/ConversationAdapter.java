package se.rooter.rooterchat;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ArrayAdapter for conversation list, extending ArrayAdapter
 */
public class ConversationAdapter extends ArrayAdapter<ConversationInfo> {

    private static final String TAG = "ConversationAdapter";

    private TextView userName;
    private TextView latestMsg;
    private TextView dateTime;
    private TextView newText;
    private ImageView avatarRound;
    private FirebaseAuth rooterAuth;

    private String latestMsgText;

    public ConversationAdapter(Context context, ArrayList<ConversationInfo> convos) {
        super(context, 0, convos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ConversationInfo convo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.conversation_list_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);
        latestMsg = (TextView) convertView.findViewById(R.id.latestMsg);
        dateTime = (TextView) convertView.findViewById(R.id.dateTime);
        newText = (TextView) convertView.findViewById(R.id.newText);

        String otherNick = convo.getOtherNickname();
        String imgpath = convo.getOtherImgPath();
        String dateEpochStr = convo.getLatestPostDate();
        String localTime;
        if (!dateEpochStr.equals("")) {
            long dateEpochLong = Long.valueOf(dateEpochStr).longValue();
            Date originalDate = new Date(dateEpochLong);
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            localTime = date.format(originalDate);
        } else {
            localTime = "No posts yet";
        }

        if (!convo.getIsViewed()) {
            newText.setVisibility(View.VISIBLE);
            dateTime.setTextColor(Color.parseColor("#FF4081"));
        } else {
            newText.setVisibility(View.GONE);
        }

        rooterAuth = FirebaseAuth.getInstance();
        if (convo.getLatestPoster().equals(rooterAuth.getCurrentUser().getUid())) {
            latestMsgText = "You: " + convo.getLatestMsg();
        } else {
            latestMsgText = convo.getLatestMsg();
        }


        rooterAuth = FirebaseAuth.getInstance();

        dateTime.setText(localTime);
        userName.setText(otherNick);
        Picasso.with(getContext()).load(imgpath).resize(40, 40).placeholder(R.drawable.ic_action_name).into(avatarRound);
        latestMsg.setText(latestMsgText);

        return convertView;
    }

}
