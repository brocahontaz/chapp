package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainChatActivity";

    private FirebaseAuth rooterAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;
    private String userID;

    private TextView footerLink;

    private TextView textViewMail;
    private TextView textViewName;
    private ImageView navpic;
    private ImageView navpicRound;

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Fragment settingsFragment;
    private Fragment homeFragment;
    private Fragment friendsFragment;
    private Fragment conversationsFragment;

    private final int radius = 5;
    private final int margin = 0;
    private final Transformation transformation = new RoundedCornersTransformation(radius, margin);



    public static Bitmap userImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsFragment = new SettingsFragment();
        homeFragment = new HomeFragment();
        friendsFragment = new FriendsFragment();
        conversationsFragment = new ConversationsFragment();

        rooterAuth = FirebaseAuth.getInstance();

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
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                // ...
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        user = rooterAuth.getCurrentUser();
        userID = user.getUid();

        footerLink = (TextView) navigationView.findViewById(R.id.footer_item_1);

        footerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);

            }
        });

        textViewMail = (TextView) header.findViewById(R.id.textViewNavMail);
        textViewName = (TextView) header.findViewById(R.id.navNick);
        textViewMail.setText(user.getEmail());
        FragmentManager fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).addToBackStack("fragBack1").commit();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        storage = FirebaseStorage.getInstance();

        View hView = navigationView.getHeaderView(0);

        navpicRound = (ImageView) hView.findViewById(R.id.profile_image);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getFragmentManager();
            setContent(fragmentManager, settingsFragment);
            return true;
        } else if (id == R.id.action_logout) {
            rooterAuth.signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            setContent(fragmentManager, homeFragment);
        } else if (id == R.id.nav_conversations) {
            setContent(fragmentManager, conversationsFragment);
        } else if (id == R.id.nav_friends) {
            setContent(fragmentManager, friendsFragment);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_settings) {
            setContent(fragmentManager, settingsFragment);
        } else if (id == R.id.nav_logout) {
            rooterAuth.signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void showData(DataSnapshot ds) {
        FirebaseUser user = rooterAuth.getCurrentUser();
        String displayName;

        for (DataSnapshot dats : ds.getChildren()) {
            UserInformation uInfo = new UserInformation();
            if(ds.child("users").child(userID).getValue(UserInformation.class) != null) {
                uInfo.setNickname(ds.child("users").child(userID).getValue(UserInformation.class).getNickname());
                uInfo.setImgPath(ds.child("users").child(userID).getValue(UserInformation.class).getImgPath());
                Picasso.with(this).load(uInfo.getImgPath()).resize(50,50).placeholder(R.drawable.ic_action_name).into(navpicRound);
                textViewName.setText(uInfo.getNickname());
            } else {
                textViewName.setText("[nameless]");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setContent(@NonNull FragmentManager fm, @NonNull Fragment fragment) {
        Fragment current = fm.findFragmentById(R.id.content_frame);
        if (current == null || !current.getClass().equals(fragment.getClass())) {
            final String tag = fragment.getClass().getSimpleName();
            fm.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(tag).commit();
        } else {
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }
}
