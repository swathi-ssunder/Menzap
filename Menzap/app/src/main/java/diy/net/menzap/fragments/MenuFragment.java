package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import diy.net.menzap.R;
import diy.net.menzap.activity.MenuCreateActivity;
import diy.net.menzap.activity.ViewMenuImage;
import diy.net.menzap.adapter.MenuAdapter;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.helper.MenuLikeCountDBHelper;
import diy.net.menzap.model.Menu;


public class MenuFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<Menu> menus;
    private SwipeRefreshLayout swipeLayout;
    private String path = Environment.getExternalStorageDirectory() + File.separator + "Menzap/Images";


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());
        SQLiteDatabase db = menuDBHelper.getReadableDatabase();
        menuDBHelper.onCreate(db);

        MenuLikeCountDBHelper menuLikeCountDBHelper= new MenuLikeCountDBHelper((getActivity()));
        db = menuLikeCountDBHelper.getReadableDatabase();
        menuLikeCountDBHelper.onCreate(db);
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

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String today = dateFormat.format(cal.getTime());

        Menu menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-13", 0, 0, 0, 1344267896, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-13", 0, 0, 0, 1314367896, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-13", 0, 0, 0, 1314267296, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-13", 0, 0, 0, 1211767896, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-13", 0, 0, 0, 1212467896, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-13", 0, 0, 0, 1422367896, 1924454896);
        menuDBHelper.insert(menu);

        /*Menu menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-10", 0, 0, 11, 1344267896, 121527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-09", 0, 0, 10, 1334767896, 131568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-09", 0, 0, 12, 1324267896, 123563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-09", 0, 0, 10, 1114767896, 143167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-09", 0, 0, 8, 1211767896, 121557896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-09", 0, 0, 11, 1494367896, 192454896);
        menuDBHelper.insert(menu);*/

        Log.d("allmenus", menuDBHelper.getAll(today).toString());
        this.menus = menuDBHelper.getAll(today);
        MenuAdapter adapter = new MenuAdapter(getActivity(), R.layout.menu, menus);

        this.handleNotification(this.menus);

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ViewMenuImage.class);
        // Pass String arrays FilePathStrings
        intent.putExtra("filepath", path+"/"+menus.get(position).getName()+".jpg");
        // Pass String arrays FileNameStrings
        intent.putExtra("filename", menus.get(position).getName());

        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton addButton = (FloatingActionButton) getView().findViewById(R.id.add);

        //Fetch sender from user profile
        SharedPreferences pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        int isAdmin = pref.getInt("isAdmin", 0);
        if(isAdmin == 0) {
            addButton.setVisibility(View.INVISIBLE);
        }

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
