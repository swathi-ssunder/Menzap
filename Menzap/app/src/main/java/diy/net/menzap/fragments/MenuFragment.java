package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import diy.net.menzap.R;
import diy.net.menzap.activity.MenuCreateActivity;
import diy.net.menzap.adapter.MenuAdapter;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.model.Event;
import diy.net.menzap.model.Menu;


public class MenuFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<Menu> menus;
    private SwipeRefreshLayout swipeLayout;


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());
        SQLiteDatabase db = menuDBHelper.getReadableDatabase();
        menuDBHelper.onCreate(db);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                MenuFragment.this.refreshView();
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
        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());
        Log.d("allmenus", menuDBHelper.getAll().toString());

        this.menus = menuDBHelper.getAll();
        MenuAdapter adapter = new MenuAdapter(getActivity(), R.layout.menu, menus);

        this.handleNotification(this.menus);

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);

        setListAdapter(adapter);
        // TODO : Implement card for menus, no drill down
        //getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), MenuDetailActivity.class);
//        intent.putExtra("MENU", this.menus.get(position));
//        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton addButton = (FloatingActionButton) getView().findViewById(R.id.add);

        //Fetch sender from user profile
        SharedPreferences pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

       /* int isAdmin = pref.getInt("isAdmin", 0);
        if(isAdmin == 0) {
            addButton.setVisibility(View.INVISIBLE);
        }*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFragment.this.addMenu();
            }
        });
    }

    protected void addMenu() {
        Intent intent = new Intent(getActivity(), MenuCreateActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            this.refreshView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleNotification(ArrayList<Menu> menus) {
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Date dateObj = new Date();
        String today = dateFormat.format(dateObj);
        for(Menu menu: menus) {
            if((menu.getIsFavourite() == 1) && (today.equals(menu.getServedOn()) )) {
                DataHolder.getInstance().getNotificationHelper().notifyForMenu(menu);
            }
        }
    }
}
