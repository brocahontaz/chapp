package se.rooter.rooterchat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rooter on 2017-04-17.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private FirebaseAuth rooterAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView textViewUser;

    private DatabaseReference databaseReference;

    private EditText editTextNickname;
    private Button buttonSave;

    private String userID;

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


        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = rooterAuth.getCurrentUser();
        userID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return myView;
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

        databaseReference.child(user.getUid()).setValue(userInfo);

        toastMessage("Nickname saved");

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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
