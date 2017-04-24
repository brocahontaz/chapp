package se.rooter.rooterchat;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class ChatFragment extends Fragment implements View.OnClickListener {

    View myView;
    ListView msgListView;

    private EditText msg_edittext;
    private String user1 = "test1";
    private String user2 = "test2";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.chat_layout, container, false);
        return myView;
    }

    @Override
    public void onClick(View v) {

    }
}
