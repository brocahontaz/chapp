package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleConversationFragment extends Fragment implements View.OnClickListener {

    private ListView conversationsList;
    private ImageView goBack;
    private EditText message;
    private ImageView postArrow;
    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private ArrayList<ChatMessage> msgs;
    private MsgAdapter msgAdapter;

    private ListView msgListView;

    private TextView receiverName;

    private UserInformation me;
    private UserInformation other;

    private String otherID;

    private String myImgPath;
    private String myNickname;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.single_conversation_layout, container, false);

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        goBack = (ImageView) myView.findViewById(R.id.backArrow);
        goBack.setOnClickListener(this);

        receiverName = (TextView) myView.findViewById(R.id.receiverName);

        message = (EditText) myView.findViewById(R.id.userMessage);
        message.setOnClickListener(this);

        postArrow = (ImageView) myView.findViewById(R.id.postMessageArrow);
        postArrow.setOnClickListener(this);

        msgListView = (ListView) myView.findViewById(R.id.msgListView);
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        msgs = new ArrayList<ChatMessage>();

        databaseReference.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("conversations").child(this.getTag()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherID = dataSnapshot.getValue(ConversationInfo.class).getParticipantTwo();

                databaseReference.child("users").child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        other = dataSnapshot.getValue(UserInformation.class);
                        receiverName.setText(other.getNickname());
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

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v == goBack) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ConversationsFragment()).addToBackStack("ConversationFragment").commit();
        } else if (v == postArrow) {
            postMessage();
        } else if (v == message) {


            if (!msgs.isEmpty()) {
                msgListView.setSelection(msgAdapter.getCount() - 1);
            }
        }
    }

    private void postMessage() {

        String senderID = rooterAuth.getCurrentUser().getUid();
        String chatMessage = message.getText().toString().trim();
        String conversationID = this.getTag();

        ChatMessage msg = new ChatMessage(senderID, chatMessage, conversationID);

        if(!chatMessage.equals("")) {

            databaseReference.child("conversations").child(this.getTag()).child("latestMsg").setValue(chatMessage);

            DatabaseReference newref = databaseReference.child("messages").push();

            newref.setValue(msg, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError dberror, DatabaseReference ref) {
                    InputMethodManager in = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(message.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    message.setText(null);
                    message.clearFocus();
                    toastMessage("Message posted");

                }
            });
        } else {
            toastMessage("Woops! Can't post empty messages");
        }

    }


    private void showData(DataSnapshot ds) {

        /*
        databaseReference.child("users").child(rooterAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(UserInformation.class);
                myImgPath = me.getImgPath();
                myNickname = me.getNickname();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        for (DataSnapshot dats : ds.getChildren()) {
            final ChatMessage msg = dats.getValue(ChatMessage.class);
            msg.setMsgID(dats.getKey());

            if(msg.getConversationID().equals(this.getTag()) && !msgs.contains(msg)) {

                /*
                if(msg.getSenderID().equals(otherID)) {
                    msg.setImgPath(other.getImgPath());
                    msg.setSenderName(other.getNickname());
                } else {
                    msg.setSenderName(myNickname);
                    msg.setImgPath(myImgPath);
                }
                */

                databaseReference.child("users").child(msg.getSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue(UserInformation.class).getNickname();
                        String imgPath = dataSnapshot.getValue(UserInformation.class).getImgPath();
                        msg.setSenderName(username);
                        msg.setImgPath(imgPath);

                        try {
                            if(getActivity() != null) {
                                msgAdapter = new MsgAdapter(getActivity(), msgs);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        msgListView.setAdapter(msgAdapter);
                        msgListView.setSelection(msgAdapter.getCount() - 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                msgs.add(msg);

            }

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
