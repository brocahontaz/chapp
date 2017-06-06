package se.rooter.rooterchat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * Fragment for the home view, in other words the channel list
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth rooterAuth;
    private DatabaseReference databaseReference;

    private ArrayList<ChatInformation> chatInfoList;
    private ArrayList<String> channels;
    private ArrayAdapter<ChatInformation> chatArrayAdapter;
    private ArrayAdapter<String> channelAdapter;

    private static final String TAG = "HomeFragment";

    private EditText editTextAddChannel;
    private ImageButton buttonAddChannel;
    private ListView channelListView;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);

        rooterAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextAddChannel = (EditText) myView.findViewById(R.id.convoInfo).findViewById(R.id.editTextAddChannel);
        buttonAddChannel = (ImageButton) myView.findViewById(R.id.convoInfo).findViewById(R.id.imgBtnStartNew);
        buttonAddChannel.setOnClickListener(this);

        channelListView = (ListView) myView.findViewById(R.id.channelListView);
        channelListView.setClickable(true);

        editTextAddChannel.clearFocus();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ChannelFragment(), data).addToBackStack(data).commit();

            }
        });

        chatInfoList = new ArrayList<ChatInformation>();


        databaseReference.child("chatChannels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        channels = new ArrayList<String>();

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
            toastMessage("Channels without a name is not allowed");
        }

    }

    private void showData(DataSnapshot ds) {
        FirebaseUser user = rooterAuth.getCurrentUser();
        String displayName;

        for (DataSnapshot dats : ds.getChildren()) {
            ChatInformation chatInfo = new ChatInformation();
            chatInfo.setChannelName(dats.getValue(ChatInformation.class).getChannelName());

            if(!channels.contains(chatInfo.getChannelName())) {
                channels.add(chatInfo.getChannelName());
            }
            if (getActivity() != null) {
                channelAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, channels);
                if(!channels.isEmpty()) {
                    channelAdapter.sort(new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return lhs.toLowerCase().compareTo(rhs.toLowerCase());
                        }
                    });
                }
            }
            channelListView.setAdapter(channelAdapter);

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
