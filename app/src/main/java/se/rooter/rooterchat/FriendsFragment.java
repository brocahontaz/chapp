package se.rooter.rooterchat;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Comparator;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Rooter on 2017-04-17.
 */

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;
    DatabaseReference dbref;

    private ListView friendsList;
    private EditText addFriend;
    private ImageButton addFriendButton;

    private ArrayList<String> friends;
    private ArrayList<UserInformation> friendsUsers;
    private ArrayAdapter<UserInformation> arrayAdapter;

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
        addFriendButton = (ImageButton) myView.findViewById(R.id.imageButtonAddFriend);

        addFriendButton.setOnClickListener(this);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        friendsUsers = new ArrayList<UserInformation>();

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference newref = databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid());

        newref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation uinfo = dataSnapshot.getValue(UserInformation.class);

                friends = uinfo.getContacts();
                if(friends != null) {
                    for (String friend : friends) {
                        databaseReference.child("users").child(friend).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                UserInformation userInfo = new UserInformation();
                                userInfo.setNickname(dataSnapshot.getValue(UserInformation.class).getNickname());
                                userInfo.setEmail(dataSnapshot.getValue(UserInformation.class).getEmail());
                                userInfo.setImgPath(dataSnapshot.getValue(UserInformation.class).getImgPath());

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
                                ArrayList<String> contacts = me.getContacts();
                                if(contacts == null) {
                                    contacts = new ArrayList<String>();
                                }

                                if(!contacts.contains(friendID)) {
                                    contacts.add(friendID);
                                    dbref.child(rooterAuth.getCurrentUser().getUid()).child("contacts").setValue(contacts, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError dberror, DatabaseReference ref) {
                                            added = true;
                                            toastMessage("Friend added, woo!");
                                        }
                                    });

                                } else {
                                    friendAlreadyExists = true;
                                    //toastMessage("Cannot add friends already in your list. ");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //toastMessage("Couldn't add friend. Be sure to provide a valid email.");
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //toastMessage("Couldn't add friend. Be sure to provide a valid email.");
            }

        });

        if(!added) {
            //toastMessageLong("Woops! Contact couldn't be added. Please provide a valid mail.");
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
