package se.rooter.rooterchat;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Rooter on 2017-04-17.
 */

public class FriendsFragment extends Fragment {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private ListView friendsList;

    private ArrayList<String> friends;
    private ArrayList<UserInformation> friendsUsers;
    private ArrayAdapter<UserInformation> arrayAdapter;

    private FriendAdapter friendAdapter;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.friends_layout, container, false);

        friendsList = (ListView) myView.findViewById(R.id.friendsList);

        friendsUsers = new ArrayList<UserInformation>();

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference newref = databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid());

        newref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation uinfo = dataSnapshot.getValue(UserInformation.class);

                friends = uinfo.getContacts();

                Log.d("test", friends.toString());

                for (String friend : friends) {
                    databaseReference.child("users").child(friend).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserInformation userInfo = new UserInformation();
                            userInfo.setNickname(dataSnapshot.getValue(UserInformation.class).getNickname());
                            userInfo.setEmail(dataSnapshot.getValue(UserInformation.class).getEmail());
                            userInfo.setImgPath(dataSnapshot.getValue(UserInformation.class).getImgPath());

                            friendsUsers.add(userInfo);

                            try {
                                if(getActivity() != null) {
                                    friendAdapter = new FriendAdapter(getActivity(), friendsUsers);
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
}
