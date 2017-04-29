package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
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
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    ImageView avatar;
    ImageView circleAvatar;

    NavigationView navigationView;

    private final int radius = 5;
    private final int margin = 0;
    private final int radiusLarge = 60;
    private final int marginLarge = 0;
    private final Transformation transformation = new RoundedCornersTransformation(radius, margin);
    private final Transformation transformationLarge = new RoundedCornersTransformation(radiusLarge, marginLarge);

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

        progressDialog = new ProgressDialog(getActivity());

        storageReference = storage.getReference().child("img").child("avatars").child(userID + "/pic");

        final Bitmap b=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        avatar = (ImageView) myView.findViewById(R.id.profileImage);
        circleAvatar = (ImageView) myView.findViewById(R.id.profile_image);
        //avatar.setImageBitmap(MainChatActivity.userImg);

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

        circleAvatar.setOnClickListener(new View.OnClickListener() {
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

            progressDialog.setMessage("Uploading avatar..");
            progressDialog.show();

            Uri file = data.getData();
            UploadTask uploadTask = storageReference.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage("Avatar failed to update, please try again");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            //Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            //MainChatActivity.userImg = img;

                            //avatar.setImageBitmap(img);
                            //View hView = navigationView.getHeaderView(0);
                            //ImageView navpic = (ImageView) hView.findViewById(R.id.userNavPic);
                            //navpic.setImageBitmap(img);

                            String imgpath = taskSnapshot.getDownloadUrl().toString();
                            //Picasso.with(getActivity()).load(imgpath).resize(110, 110).centerCrop().transform(transformation).placeholder(R.drawable.ic_action_name).into(avatar);
                            //Picasso.with(getActivity()).load(imgpath).resize(50, 50).centerCrop().transform(transformation).placeholder(R.drawable.ic_action_name).into(navpic);

                            saveUserImgPath(imgpath);

                            //UserInformation updateUser = new UserInformation(userID, imgpath);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                    progressDialog.dismiss();
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

        databaseReference.child("users").child(user.getUid()).child("nickname").setValue(nickname);

        toastMessage("Nickname saved");

    }

    private void saveUserImgPath(String path) {

        databaseReference.child("users").child(userID).child("imgPath").setValue(path);

    }

    private void showData(DataSnapshot ds) {
        FirebaseUser user = rooterAuth.getCurrentUser();
        String displayName;

        for (DataSnapshot dats : ds.getChildren()) {
            UserInformation uInfo = new UserInformation();
            if(ds.child("users").child(userID).getValue(UserInformation.class) != null) {
                uInfo.setNickname(ds.child("users").child(userID).getValue(UserInformation.class).getNickname());
                uInfo.setImgPath(ds.child("users").child(userID).getValue(UserInformation.class).getImgPath());
                textViewUser.setText(uInfo.getNickname());
                //Picasso.with(getActivity()).load(uInfo.getImgPath()).resize(120, 120).centerCrop().transform(transformation).placeholder(R.drawable.ic_action_name).into(avatar);
                Picasso.with(getActivity()).load(uInfo.getImgPath()).placeholder(R.drawable.ic_action_name).into(circleAvatar);
            } else {
                textViewUser.setText(user.getEmail());
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
