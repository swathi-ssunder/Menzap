package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.adapter.UserAdapter;
import diy.net.menzap.helper.UserDBHelper;
import diy.net.menzap.model.User;


public class FriendsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<User> users;
    private SwipeRefreshLayout swipeLayout;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDBHelper userDBHelper = new UserDBHelper(getActivity());
        SQLiteDatabase db = userDBHelper.getReadableDatabase();
        userDBHelper.onCreate(db);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                FriendsFragment.this.refreshView();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshView();
    }

    private void refreshView() {
        UserDBHelper userDBHelper = new UserDBHelper(getActivity());
        Log.d("allfriends", userDBHelper.getAll().toString());

        this.users = userDBHelper.getAll();
        UserAdapter adapter = new UserAdapter(getActivity(), R.layout.user, users);

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);

        setListAdapter(adapter);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            this.refreshView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onSelected() {

    }
}
