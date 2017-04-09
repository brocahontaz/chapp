package se.rooter.rooterchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatActivity";

    private FirebaseAuth rooterAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView textViewUser;
    private Button buttonLogout;

    private DatabaseReference databaseReference;

    private EditText editTextNickname;
    private Button buttonSave;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rooterAuth = FirebaseAuth.getInstance();

        /*
        if (rooterAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out");
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                // ...
            }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        FirebaseUser user = rooterAuth.getCurrentUser();
        userID = user.getUid();

        textViewUser = (TextView) findViewById(R.id.textViewUser);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            rooterAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view == buttonSave) {
            saveUserInfo();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        rooterAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            rooterAuth.removeAuthStateListener(mAuthListener);
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

        databaseReference.child(user.getUid()).setValue(userInfo);

        Toast.makeText(this, "Nickname saved", Toast.LENGTH_LONG).show();

    }

    private void showData(DataSnapshot ds) {
        FirebaseUser user = rooterAuth.getCurrentUser();
        String displayName;

        for (DataSnapshot dats : ds.getChildren()) {
            UserInformation uInfo = new UserInformation();
            if(ds.child(userID).getValue(UserInformation.class) != null) {
                uInfo.setNickname(ds.child(userID).getValue(UserInformation.class).getNickname());
                    textViewUser.setText(uInfo.getNickname());
            } else {
                textViewUser.setText(user.getEmail());
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
