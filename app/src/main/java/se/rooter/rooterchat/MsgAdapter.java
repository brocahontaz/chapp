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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MsgAdapter extends ArrayAdapter<ChatMessage> {

    private static final String TAG = "MsgAdapter";

    private DatabaseReference databaseReference;
    private UserInformation user;
    private TextView userName;
    private ImageView avatarRound;
    private TextView messageTime;


    public MsgAdapter(Context context, ArrayList<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ChatMessage chatMsg = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_message_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);
        TextView message = (TextView) convertView.findViewById(R.id.userMessage);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);
        messageTime = (TextView) convertView.findViewById(R.id.messageTime);

        String thisUserId = chatMsg.getSenderID();
        String imgPath = chatMsg.getImgPath();

        String dateEpochStr = chatMsg.getPostDate();
        long dateEpochLong = Long.valueOf(dateEpochStr).longValue();
        Date originalDate = new Date(dateEpochLong);

        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String localTime = date.format(originalDate);

        messageTime.setText(localTime);

        Picasso.with(getContext()).load(imgPath).resize(50,50).placeholder(R.drawable.ic_action_name).into(avatarRound);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = new UserInformation();

        userName.setText(chatMsg.getSenderName());
        message.setText(chatMsg.getMessage());
        //avatar.setImageBitmap(channelMessage.getImg());


        return convertView;
    }

}
