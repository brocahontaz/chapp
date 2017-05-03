package se.rooter.rooterchat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendInfoDialog extends DialogFragment {

    private AlertDialog.Builder builder;
    private DatabaseReference databaseReference;
    private FirebaseAuth rooterAuth;
    private String id;
    private int position;
    private FriendAdapter friendAdapter;

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

}
