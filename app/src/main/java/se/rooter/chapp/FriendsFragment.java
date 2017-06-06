package se.rooter.chapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Fragment for the contact list
 */

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;
    DatabaseReference dbref;

    private ListView friendsList;
    private EditText addFriend;
    private ImageButton addFriendButton;
    private ImageView removeFriend;

    private ArrayList<String> friends;
    private ArrayList<UserInformation> friendsUsers;
    private ArrayAdapter<UserInformation> arrayAdapter;
    private HashSet<String> friendSet;
    private HashMap<String, Object> friendMap;

    private FriendAdapter friendAdapter;

    private UserInformation me;

    private boolean added;
    private boolean friendAlreadyExists;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.friends_layout, container, false);

        friendsList = (ListView) myView.findViewById(R.id.friendsList);

        addFriend = (EditText) myView.findViewById(R.id.editTextAddFriend);
        addFriendButton = (ImageButton) myView.findViewById(R.id.imgBtnStartNew);

        addFriendButton.setOnClickListener(this);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        friendsUsers = new ArrayList<UserInformation>();

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInformation data = (UserInformation) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", data.getId());
                bundle.putString("name", data.getNickname());
                bundle.putInt("position", position);
                FriendInfoDialog newFragment = new FriendInfoDialog();
                newFragment.setArguments(bundle);
                newFragment.setAdapter(friendAdapter);
                newFragment.show(getFragmentManager(), data.getId());
            }
        });

        DatabaseReference newref = databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid());


        DatabaseReference newref2 = databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid()).child("contacts");

        newref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String key = ds.getKey();

                    databaseReference.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserInformation userInfo = new UserInformation();
                            userInfo.setNickname(dataSnapshot.getValue(UserInformation.class).getNickname());
                            userInfo.setEmail(dataSnapshot.getValue(UserInformation.class).getEmail());
                            userInfo.setImgPath(dataSnapshot.getValue(UserInformation.class).getImgPath());
                            userInfo.setId(key);

                            if(!friendsUsers.contains(userInfo)) {
                                friendsUsers.add(userInfo);
                            }

                            try {
                                if (getActivity() != null) {
                                    friendAdapter = new FriendAdapter(getActivity(), friendsUsers);
                                    if(!friendsUsers.isEmpty()) {
                                        Collections.sort(friendsUsers, new UserComparator());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            friendsList.setAdapter(friendAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v == addFriendButton) {
            addFriend();
        }
    }

    private void addFriend() {
        added = false;
        final String friendMail = addFriend.getText().toString().trim();

        dbref = FirebaseDatabase.getInstance().getReference().child("users");

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserInformation ui = ds.getValue(UserInformation.class);
                    String mail = ds.getValue(UserInformation.class).getEmail();
                    final String friendID = ds.getKey();

                    if(ui.getEmail().equals(friendMail)) {

                        dbref.child(rooterAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                me = dataSnapshot.getValue(UserInformation.class);
                                HashMap<String, Object> contactsMap = me.getContacts();

                                if (contactsMap == null) {
                                    contactsMap = new HashMap<String, Object>();
                                }

                                if (!contactsMap.containsKey(friendID)) {
                                    contactsMap.put(friendID, true);
                                    dbref.child(rooterAuth.getCurrentUser().getUid()).child("contacts").updateChildren(contactsMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            toastMessage("Woo! Contact added :D");
                                        }
                                    });
                                } else {
                                    toastMessage("Woops! Contact is already in your list :)");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        if(!added) {

        }

        if (friendAlreadyExists) {
            toastMessage("Woops! Contact already in list.");
        }


        InputMethodManager in = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(addFriend.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        addFriend.setText(null);
        addFriend.clearFocus();
    }

    private void toastMessage(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void toastMessageLong(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }
}
