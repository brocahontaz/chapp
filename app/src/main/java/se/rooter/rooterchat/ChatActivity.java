package se.rooter.rooterchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth rooterAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;

    private EditText editTextNickname;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rooterAuth = FirebaseAuth.getInstance();

        if (rooterAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        FirebaseUser user = rooterAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUser);

        String displayName;

        textViewUserEmail.setText("Welcome " + user.getEmail());

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

    private void saveUserInfo() {
        String nickname = editTextNickname.getText().toString().trim();

        UserInformation userInfo = new UserInformation(nickname);

        FirebaseUser user = rooterAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInfo);

        Toast.makeText(this, "Nickname saved", Toast.LENGTH_LONG).show();
    }
}
