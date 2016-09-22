package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import diy.net.menzap.R;
import diy.net.menzap.activity.TabsActivity;


public class MenuFragment extends Fragment{

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button submitButton = (Button) getView().findViewById(R.id.submit);
        Log.d("button", submitButton.toString());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFragment.this.submit();
            }
        });
    }

    protected void submit() {
        EditText editText = (EditText) getView().findViewById(R.id.reviewText);
        String reviewText = editText.getText().toString();

        ((TabsActivity)getActivity()).saveAndPublish(reviewText);
    }
}