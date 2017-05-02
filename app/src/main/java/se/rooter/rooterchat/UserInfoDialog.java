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

public class UserInfoDialog extends DialogFragment {

    private DatabaseReference dbref;
    private String email;
    private AlertDialog.Builder builder;
    private ArrayList<String> contacts;
    private HashMap<String, Object> contactsMap;
    private String userID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        String userMail = bundle.getString("userMail");
        String thisUserId = bundle.getString("thisUserId");
        dbref = FirebaseDatabase.getInstance().getReference().child("users").child(thisUserId);


        dbref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                //contacts = dataSnapshot.getValue(UserInformation.class).getContacts();
                contactsMap = dataSnapshot.getValue(UserInformation.class).getContacts();

                if (contactsMap == null) {
                    contactsMap = new HashMap<String, Object>();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        builder.setMessage("Add " + userMail + " as a friend?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(!contactsMap.containsKey(userID)) {
                            contactsMap.put(userID, true);
                            dbref.child("contacts").updateChildren(contactsMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                }
                            });
                            toastMessage("Woo! Contact added :D");
                        } else {
                            toastMessage("Woops! Contact is already in your list :)");
                        }

                        /*
                        if(contacts == null) {
                            contacts = new ArrayList<String>();
                        }
                        if(!contacts.contains(userID)) {
                            contacts.add(userID);
                            toastMessage("Added contact, woo!");
                        } else {
                            toastMessage("Woops! Contact is already in friendslist.");
                        }

                        dbref.child("contacts").setValue(contacts);
                        */
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        // Use the Builder class for convenient dialog construction


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void toastMessage(String message) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
