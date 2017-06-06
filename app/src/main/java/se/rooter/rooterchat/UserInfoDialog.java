package se.rooter.rooterchat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for the dialog when adding users to contactlist in a channel
 */

public class UserInfoDialog extends DialogFragment {

    private DatabaseReference dbref;
    private AlertDialog.Builder builder;
    private HashMap<String, Object> contactsMap;
    private String userID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**
         * Create new alert dialog builder
         */
        builder = new AlertDialog.Builder(getActivity());

        /**
         * Get values from passed bundle
         */
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        String userMail = bundle.getString("userMail");
        String thisUserId = bundle.getString("thisUserId");

        /*Get database reference */
        dbref = FirebaseDatabase.getInstance().getReference().child("users").child(thisUserId);

        /* Add event listener and get contacts */
        dbref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactsMap = dataSnapshot.getValue(UserInformation.class).getContacts();

                /* If there's no contactlist, create a new one */
                if (contactsMap == null) {
                    contactsMap = new HashMap<String, Object>();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /* Set message and buttons for builder*/
        builder.setMessage("Add " + userMail + " as a friend?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /* Add contact if ok is pressed, if the contact is not already in the list */
                        if (!contactsMap.containsKey(userID)) {
                            contactsMap.put(userID, true);
                            dbref.child("contacts").updateChildren(contactsMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                }
                            });

                            /*Display status message for success */
                            toastMessage("Woo! Contact added :D");
                        } else {

                            /* Display status message for contact already in list */
                            toastMessage("Woops! Contact is already in your list :)");
                        }

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }

    private void toastMessage(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
