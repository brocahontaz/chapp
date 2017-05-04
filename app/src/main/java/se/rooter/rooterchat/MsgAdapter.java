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

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MsgAdapter extends ArrayAdapter<ChatMessage> {

    private static final String TAG = "MsgAdapter";

    private DatabaseReference databaseReference;
    private UserInformation user;
    private TextView userName;
    private ImageView avatar;
    private ImageView avatarRound;
    private String nick = "";
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<ImageView> imgs;
    private Context context;
    private final int radius = 5;
    private final int margin = 5;
    private final Transformation transformation = new RoundedCornersTransformation(radius, margin);

    public MsgAdapter(Context context, ArrayList<ChatMessage> messages) {
        super(context, 0, messages);
        imgs = new ArrayList<ImageView>();
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
        ImageView userAvatar = (ImageView) convertView.findViewById(R.id.userAvatar);
        avatar = (ImageView) convertView.findViewById(R.id.userAvatar);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);

        String thisUserId = chatMsg.getSenderID();
        String imgPath = chatMsg.getImgPath();

        Picasso.with(getContext()).load(imgPath).resize(50,50).placeholder(R.drawable.ic_action_name).into(avatarRound);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = new UserInformation();

        userName.setText(chatMsg.getSenderName());
        message.setText(chatMsg.getMessage());
        //avatar.setImageBitmap(channelMessage.getImg());


        return convertView;
    }

}
