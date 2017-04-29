package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class ChannelFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "ChannelFragment";

    private String msgNick;

    private TextView channelName;
    private ImageView goBack;

    private EditText message;
    private ImageView postArrow;

    private HashSet<ChannelMessage> msgSet;
    private ArrayList<ChannelMessage> msgs;
    private ChannelAdapter msgAdapter;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private ListView channelListView;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.channel_layout, container, false);

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        channelName = (TextView) myView.findViewById(R.id.channelName);
        channelName.setText("#" + this.getTag());

        goBack = (ImageView) myView.findViewById(R.id.backArrow);
        goBack.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        message = (EditText) myView.findViewById(R.id.userMessage);
        message.setOnClickListener(this);

        postArrow = (ImageView) myView.findViewById(R.id.postMessageArrow);
        postArrow.setOnClickListener(this);

        channelListView = (ListView) myView.findViewById(R.id.channelListView);
        channelListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //channelListView.setStackFromBottom(true);

        msgs = new ArrayList<ChannelMessage>();


        databaseReference.child("chatChannelMessages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
/*
        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (actionId == message.IME_ACTION_DONE)) {
                    InputMethodManager in = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(message.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
*/
        return myView;
    }


    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == goBack) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).addToBackStack("HomeFragment").commit();
        } else if (view == postArrow) {
            postMessage();
        } else if (view == message) {

            if (!msgs.isEmpty()) {
                channelListView.setSelection(msgAdapter.getCount() - 1);
            }
        }
    }

    private void postMessage() {

        String chatMessage = message.getText().toString().trim();
        ChannelMessage msg = new ChannelMessage(rooterAuth.getCurrentUser().getUid(), chatMessage, this.getTag());

        if(!chatMessage.equals("")) {
            DatabaseReference newRef = databaseReference.child("chatChannelMessages").push();

            newRef.setValue(msg, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError dberror, DatabaseReference ref) {
                    InputMethodManager in = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(message.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    message.setText(null);
                    message.clearFocus();
                    toastMessage("Message posted");

                }
            });

            /*
            newRef.setValue(msg, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError dberror, DatabaseReference ref) {
                    if(dberror == null) {
                        toastMessage("Message posted");
                    } else {
                        toastMessage("Woops! Something went wrong");
                    }
                }
            });*/
        } else {
            toastMessage("Woops! Can't post empty messages");

        }

    }

    private void showData(DataSnapshot ds) {

        for (DataSnapshot dats : ds.getChildren()) {
            final ChannelMessage msg = dats.getValue(ChannelMessage.class);
            msg.setMsgID(dats.getKey());
            if(msg.getChannel().equals(this.getTag())) {
            databaseReference.child("users").child(msg.getSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.getValue(UserInformation.class).getNickname();
                    String imgPath = dataSnapshot.getValue(UserInformation.class).getImgPath();
                    //toastMessage(username);
                    msg.setSenderName(username);
                    msg.setImgPath(imgPath);
                    try {
                        if(getActivity() != null) {
                            msgAdapter = new ChannelAdapter(getActivity(), msgs);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    channelListView.setAdapter(msgAdapter);
                    channelListView.setSelection(msgAdapter.getCount() - 1);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //toastMessage(msg.getMessage());
            //if(msg.getChannel().equals(this.getTag())) {
                if(!msgs.contains(msg)) {
                    msgs.add(msg);
                }
            }



            //channelListView.setAdapter(msgAdapter);

        }
    }
}
