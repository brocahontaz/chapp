package se.rooter.rooterchat;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private FirebaseAuth rooterAuth;

    private TextView textViewUser;

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private EditText editTextNickname;
    private Button buttonSave;

    private String userID;

    ImageView avatar;

    NavigationView navigationView;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_layout, container, false);

        rooterAuth = FirebaseAuth.getInstance();

        buttonSave = (Button) myView.findViewById(R.id.buttonSave);
        editTextNickname = (EditText) myView.findViewById(R.id.editTextNickname);
        textViewUser = (TextView) myView.findViewById(R.id.textViewUser);
        buttonSave.setOnClickListener(this);

        storage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = rooterAuth.getCurrentUser();
        userID = user.getUid();

        storageReference = storage.getReference().child("img").child("avatars").child(userID + "/pic");

        final Bitmap b=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        avatar = (ImageView) myView.findViewById(R.id.profileImage);
        avatar.setImageBitmap(MainChatActivity.userImg);
/*
        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                avatar.setImageBitmap(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
        */


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgintent = new Intent();

                imgintent.setType("image/*");
                imgintent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgintent, "Select image"), 1);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri file = data.getData();
            UploadTask uploadTask = storageReference.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage("Avatar failed to update, please try again");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            MainChatActivity.userImg = img;
                            avatar.setImageBitmap(img);
                            View hView = navigationView.getHeaderView(0);
                            ImageView navpic = (ImageView) hView.findViewById(R.id.userNavPic);
                            navpic.setImageBitmap(img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                    toastMessage("Avatar updated");
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

        if (view == buttonSave) {
            saveUserInfo();
        }
    }

    private void saveUserInfo() {

        // Updates the user attributes:

        String nickname = editTextNickname.getText().toString().trim();

        FirebaseUser user = rooterAuth.getCurrentUser();

        if(nickname.equals("")) {
            nickname = user.getEmail();
        }

        UserInformation userInfo = new UserInformation(nickname);

        databaseReference.child("users").child(user.getUid()).setValue(userInfo);

        toastMessage("Nickname saved");

    }

    private void showData(DataSnapshot ds) {
        FirebaseUser user = rooterAuth.getCurrentUser();
        String displayName;

        for (DataSnapshot dats : ds.getChildren()) {
            UserInformation uInfo = new UserInformation();
            if(ds.child("users").child(userID).getValue(UserInformation.class) != null) {
                uInfo.setNickname(ds.child("users").child(userID).getValue(UserInformation.class).getNickname());
                textViewUser.setText(uInfo.getNickname());
            } else {
                textViewUser.setText(user.getEmail());
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
