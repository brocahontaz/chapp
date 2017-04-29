package se.rooter.rooterchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth rooterAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbref = FirebaseDatabase.getInstance().getReference();

        rooterAuth = FirebaseAuth.getInstance();

        /*
        if (rooterAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if(!rooterAuth.getCurrentUser().isEmailVerified()) {
                        //toastMessage("Please verify your account");
                        //rooterAuth.signOut();
                        //finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {

                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainChatActivity.class));
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }

        if (view == textViewSignin) {
            startActivity(new Intent(this, LoginActivity.class));
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

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Email empty
        if (TextUtils.isEmpty(email)) {
            toastMessage("Please enter an email");
            // Stopping further execution
            return;
        }

        // Password empty
        if(TextUtils.isEmpty(password)) {
            toastMessage("Please enter a password");
            // Stopping further execution
            return;
        }

        // Valid input, continue

        progressDialog.setMessage("Registering user..");
        progressDialog.show();

        rooterAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Successfully registered and logged in
                if(task.isSuccessful()) {
                    UserInformation userInfo = new UserInformation(rooterAuth.getCurrentUser().getEmail());
                    dbref.child("users").child(rooterAuth.getCurrentUser().getUid()).setValue(userInfo);
                    rooterAuth.getCurrentUser().sendEmailVerification();
                    progressDialog.dismiss();
                    rooterAuth.signOut();
                    toastMessage("Registered successfully. Please verify account via verification mail before logging in.");
                    //finish();
                    //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

                    // Registering not working
                } else {
                    toastMessage("Failed to register, please try again");
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
