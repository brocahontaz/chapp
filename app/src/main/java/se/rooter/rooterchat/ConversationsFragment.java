package se.rooter.rooterchat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ConversationsFragment extends Fragment {

    private EditText newConvo;
    private ImageButton imgBtnStartNew;
    private ListView conversationsList;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.conversations_layout, container, false);

        newConvo = (EditText) myView.findViewById(R.id.editTextNewConvo);
        imgBtnStartNew = (ImageButton) myView.findViewById(R.id.imgBtnStartNew);

        imgBtnStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return myView;
    }
}
