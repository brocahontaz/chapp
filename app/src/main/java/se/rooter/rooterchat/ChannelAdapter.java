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

import java.util.ArrayList;

public class ChannelAdapter extends ArrayAdapter<ChannelMessage> {

    private static final String TAG = "ChannelAdapter";

    private DatabaseReference databaseReference;
    private UserInformation user;
    private TextView userName;
    private ImageView avatar;
    private String nick = "";
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<ImageView> imgs;

    public ChannelAdapter(Context context, ArrayList<ChannelMessage> messages) {
        super(context, 0, messages);
        imgs = new ArrayList<ImageView>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ChannelMessage channelMessage = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_message_item, parent, false);
        }

        userName = (TextView) convertView.findViewById(R.id.userNickname);
        TextView message = (TextView) convertView.findViewById(R.id.userMessage);
        ImageView userAvatar = (ImageView) convertView.findViewById(R.id.userAvatar);
        avatar = (ImageView) convertView.findViewById(R.id.userAvatar);

        String thisUserId = channelMessage.getSenderID();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("img").child("avatars").child(thisUserId + "/pic");



/*
        try {
                if(!MainChatActivity.chappdb.checkIfRecordExists(thisUserId)) {
                    storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            MainChatActivity.chappdb.insertData(channelMessage.getSenderID(), img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });

                }

            avatar.setImageBitmap(MainChatActivity.chappdb.getData(thisUserId));

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
 */
        /*
        if(MainChatActivity.chappdb.checkIfRecordExists(thisUserId)) {

        }*/


        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = new UserInformation();

        userName.setText(channelMessage.getSenderName());
        message.setText(channelMessage.getMessage());
        //avatar.setImageBitmap(channelMessage.getImg());


        return convertView;
    }

    private void showData(DataSnapshot ds) {

    }
}
