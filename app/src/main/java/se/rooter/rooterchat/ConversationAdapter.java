package se.rooter.rooterchat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ConversationAdapter extends ArrayAdapter<ConversationInfo> {

    private static final String TAG = "ConversationAdapter";

    private TextView userName;
    private TextView latestMsg;
    private ImageView avatar;
    private ImageView avatarRound;

    public ConversationAdapter(Context context, ArrayList<ConversationInfo> convos) {
        super(context, 0, convos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ConversationInfo convo = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.conversation_list_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);
        latestMsg = (TextView)convertView.findViewById(R.id.latestMsg);

        String otherNick = convo.getOtherNickname();
        String imgpath = convo.getOtherImgPath();
        String latestMsgText = convo.getLatestMsg();

        //Log.d("name", otherNick);

        userName.setText(otherNick);
        Picasso.with(getContext()).load(imgpath).resize(40,40).placeholder(R.drawable.ic_action_name).into(avatarRound);
        latestMsg.setText(latestMsgText);

        return convertView;
    }

}
