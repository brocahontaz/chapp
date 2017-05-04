package se.rooter.rooterchat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FriendInfoDialog extends DialogFragment {

    private AlertDialog.Builder builder;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseAuth rooterAuth;
    private String id;
    private int position;
    private FriendAdapter friendAdapter;
    private HashMap<String, Object> convoMap;
    private boolean hasConvo;
    String convoID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        String nickname = bundle.getString("name");
        position = bundle.getInt("position");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.friend_dialog, null);

        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText("Actions");

        TextView message = (TextView) layout.findViewById(R.id.message);
        message.setText("What do you wanna do with " + nickname + "?");

        id = bundle.getString("id");

        TextView newMsg = (TextView) layout.findViewById(R.id.newMsg);
        newMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConversation();
            }
        });

        TextView remove = (TextView) layout.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rooterAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(rooterAuth.getCurrentUser().getUid()).child("contacts").child(id);

                databaseReference.removeValue();

                friendAdapter.remove(friendAdapter.getItem(position));
                friendAdapter.notifyDataSetChanged();

                FriendInfoDialog.this.dismiss();

                toastMessage("Friend removed. :'(");
            }
        });


        builder.setView(layout);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    public void setAdapter(FriendAdapter friendAdapter) {
        this.friendAdapter = friendAdapter;
    }

    private void toastMessage(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void startTalking() {
        rooterAuth = FirebaseAuth.getInstance();

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("conversations");
        DatabaseReference newRef2 = databaseReference2.push();

        ConversationInfo convoInfo = new ConversationInfo(rooterAuth.getCurrentUser().getUid(), id);
        newRef2.setValue(convoInfo);

        convoID = newRef2.getKey();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(rooterAuth.getCurrentUser().getUid()).child("conversations").child(newRef2.getKey());

        convoMap = new HashMap<String, Object>();
        convoMap.put(id, true);
        databaseReference.updateChildren(convoMap);

        dismiss();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new SingleConversationFragment(), convoID).addToBackStack(convoID).commit();

    }

    private void startConversation() {
        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(rooterAuth.getCurrentUser().getUid()).child("conversations");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean hasConvo = false;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if(ds.hasChild(id)) {

                        hasConvo = true;
                        convoID = ds.getKey();

                    }
                }

                if(!hasConvo) {
                    startTalking();
                } else {
                    dismiss();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new SingleConversationFragment(), convoID).addToBackStack(convoID).commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
