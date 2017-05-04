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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private String latestMsgText;

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

        rooterAuth = FirebaseAuth.getInstance();
        if(convo.getLatestPoster().equals(rooterAuth.getCurrentUser().getUid())) {
            latestMsgText = "You: ";
        }

        latestMsgText += convo.getLatestMsg();

        rooterAuth = FirebaseAuth.getInstance();
        /*databaseReference = FirebaseDatabase.getInstance().getReference().child("conversations").child(convo.getId());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latestMsgText = dataSnapshot.getValue(ConversationInfo.class).getLatestMsg();
                latestMsg.setText(latestMsgText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //Log.d("name", otherNick);

        userName.setText(otherNick);
        Picasso.with(getContext()).load(imgpath).resize(40,40).placeholder(R.drawable.ic_action_name).into(avatarRound);
        latestMsg.setText(latestMsgText);

        return convertView;
    }

}
