package se.rooter.rooterchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity for login
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private Button buttonLogin;
    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth rooterAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rooterAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if(!rooterAuth.getCurrentUser().isEmailVerified()) {
                        toastMessage("Please verify your account");
                        rooterAuth.signOut();
                        //finish();
                       startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        // User is signed in
                        toastMessage("Successfully signed in with " + user.getEmail());
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainChatActivity.class));
                    }
                } else {
                    // User is signed out
                }
                // ...
            }
        };

        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        progressDialog = new ProgressDialog(this);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        }

        if (view == textViewSignup) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
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

    private void userLogin() {
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

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

        progressDialog.setMessage("Signing in user..");
        progressDialog.show();

        rooterAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    finish();
                    //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                } else {
                    toastMessage("Failed to login. Please enter correct credentials");
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
