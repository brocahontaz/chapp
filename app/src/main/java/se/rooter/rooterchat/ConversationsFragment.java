package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Fragment for the conversation list
 */
public class ConversationsFragment extends Fragment {

    private EditText newConvo;
    private ImageButton imgBtnStartNew;
    private ListView conversationsList;
    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private ArrayList<ConversationInfo> conversations;
    private ConversationAdapter convoAdapter;

    private String userID;
    private boolean isViewed;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.conversations_layout, container, false);

        newConvo = (EditText) myView.findViewById(R.id.editTextNewConvo);
        imgBtnStartNew = (ImageButton) myView.findViewById(R.id.imgBtnStartNew);

        conversationsList = (ListView) myView.findViewById(R.id.conversationsList);

        conversations = new ArrayList<ConversationInfo>();
        try {
            if (getActivity() != null) {
                convoAdapter = new ConversationAdapter(getActivity(), conversations);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid()).child("conversations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    final String convoKey = ds.getKey();
                    final ConversationInfo convoInfo = new ConversationInfo();
                    final boolean viewed = false;
                    convoInfo.setId(convoKey);


                    databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid()).child("conversations").child(convoKey).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            convoInfo.setIsViewed(dataSnapshot.getValue(boolean.class));
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    for(DataSnapshot dats : ds.getChildren()) {

                        userID = dats.getKey();
                        convoInfo.setIsViewed(dats.getValue(boolean.class));

                    }

                    databaseReference.child("users").child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String nickname = dataSnapshot.getValue(UserInformation.class).getNickname();
                            String imgpath = dataSnapshot.getValue(UserInformation.class).getImgPath();
                            convoInfo.setOtherNickname(nickname);
                            convoInfo.setOtherImgPath(imgpath);

                            databaseReference.child("conversations").child(convoKey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    convoInfo.setLatestMsg(dataSnapshot.getValue(ConversationInfo.class).getLatestMsg());
                                    convoInfo.setLatestPoster(dataSnapshot.getValue(ConversationInfo.class).getLatestPoster());
                                    convoInfo.setLatestPostDate(dataSnapshot.getValue(ConversationInfo.class).getLatestPostDate());

                                    if(!conversations.contains(convoInfo)) {
                                        conversations.add(convoInfo);
                                        if(convoAdapter != null) {
                                            convoAdapter.notifyDataSetChanged();
                                        }

                                    }

                                    conversationsList.setAdapter(convoAdapter);
                                    if(!conversations.isEmpty()) {
                                        Collections.sort(conversations, new ConversationComparator());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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

        conversationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversationInfo data = (ConversationInfo) parent.getItemAtPosition(position);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SingleConversationFragment(), data.getId()).addToBackStack(data.getId()).commit();
            }
        });

        imgBtnStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return myView;
    }
}
