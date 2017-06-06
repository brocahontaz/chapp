package se.rooter.rooterchat;


import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

/**
 * ArrayAdapter for the chatchannels
 */

public class ChannelAdapter extends ArrayAdapter<ChannelMessage> {

    private static final String TAG = "ChannelAdapter";

    private DatabaseReference databaseReference;
    private UserInformation user;
    private TextView userName;
    private ImageView avatar;
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

        final ChannelMessage channelMessage = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_message_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);
        TextView message = (TextView) convertView.findViewById(R.id.userMessage);
        ImageView userAvatar = (ImageView) convertView.findViewById(R.id.userAvatar);
        avatar = (ImageView) convertView.findViewById(R.id.userAvatar);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);

        String imgPath = channelMessage.getImgPath();

        Picasso.with(getContext()).load(imgPath).resize(50, 50).placeholder(R.drawable.ic_action_name).into(avatarRound);

        user = new UserInformation();

        userName.setText(channelMessage.getSenderName());
        message.setText(channelMessage.getMessage());

        return convertView;
    }

}
