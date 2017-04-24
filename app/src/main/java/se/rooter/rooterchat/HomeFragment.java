package se.rooter.rooterchat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "HomeFragment";

    private EditText editTextAddChannel;
    private ImageButton buttonAddChannel;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextAddChannel = (EditText) myView.findViewById(R.id.addChannelSub).findViewById(R.id.editTextAddChannel);
        buttonAddChannel = (ImageButton) myView.findViewById(R.id.addChannelSub).findViewById(R.id.imageButtonAddChannel);
        buttonAddChannel.setOnClickListener(this);


        return myView;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAddChannel) {
            addChannel();
        }
    }

    private void addChannel() {

        String channel = editTextAddChannel.getText().toString().trim();

        ChatInformation chatInfo = new ChatInformation(channel);

        if(!channel.equals("")) {

            databaseReference.child("chatChannels").child(channel).setValue(chatInfo, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError dberror, DatabaseReference ref) {
                    if(dberror == null) {
                        toastMessage("Channel added");
                    } else {
                        toastMessage("Woops! Can't add duplicate channels");
                    }
                }
            });
        } else {
            toastMessage("Channel couldn't be added");
        }




    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
