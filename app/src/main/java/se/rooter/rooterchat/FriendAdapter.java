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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<UserInformation> {

    private TextView userName;
    private TextView userMail;
    private ImageView avatarRound;

    public FriendAdapter(Context context, ArrayList<UserInformation> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final UserInformation user = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);

        userMail = (TextView) convertView.findViewById(R.id.userMail);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);

        String imgpath = user.getImgPath();
        String username = user.getNickname();

        Log.d("name", username);

        String email = user.getEmail();

        Picasso.with(getContext()).load(imgpath).resize(40,40).placeholder(R.drawable.ic_action_name).into(avatarRound);

        userName.setText(username);
        userMail.setText(email);


        return convertView;

    }
}

