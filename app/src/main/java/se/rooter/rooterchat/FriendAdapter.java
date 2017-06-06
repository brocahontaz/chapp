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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * ArrayAdapter for the contact list, extending ArrayAdapter
 */
public class FriendAdapter extends ArrayAdapter<UserInformation> {

    private TextView userName;
    private TextView userMail;
    private ImageView avatarRound;
    private ImageView removeFriend;

    public FriendAdapter(Context context, ArrayList<UserInformation> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final UserInformation user = getItem(position);
        final int pos2 = position;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_list_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);

        userMail = (TextView) convertView.findViewById(R.id.userMail);
        avatarRound = (ImageView) convertView.findViewById(R.id.profile_image);
        removeFriend = (ImageView) convertView.findViewById(R.id.removeFriend);
        removeFriend.setVisibility(View.GONE);

        final String id = user.getId();

        String imgpath = user.getImgPath();
        String username = user.getNickname();

        String email = user.getEmail();

        Picasso.with(getContext()).load(imgpath).resize(40,40).placeholder(R.drawable.ic_action_name).into(avatarRound);

        userName.setText(username);
        userMail.setText(email);


        return convertView;

    }
}

