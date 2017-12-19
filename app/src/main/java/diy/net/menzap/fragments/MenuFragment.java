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

        /*Menu menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-13", 0, 0, 15, 1344267896, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-13", 0, 0, 10, 1314367896, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-13", 0, 0, 3, 1314267296, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-13", 0, 0, 5, 1211767896, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-13", 0, 0, 9, 1212467896, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-13", 0, 0, 6, 1422367896, 1924454896);
        menuDBHelper.insert(menu);



        menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-12", 0, 0, 11, 1344267897, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-12", 0, 0, 8, 1314367897, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-12", 0, 0, 7, 1314267297, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-12", 0, 0, 4, 1211767897, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-12", 0, 0, 9, 1212467897, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-12", 0, 0, 4, 1422367897, 1924454896);
        menuDBHelper.insert(menu);



        menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-11", 0, 0, 9, 1344267892, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-11", 0, 0, 5, 1314367892, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-11", 0, 0, 10, 1314267292, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-11", 0, 0, 4, 1211767892, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-11", 0, 0, 10, 1212467892, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-11", 0, 0, 5, 1422367892, 1924454896);
        menuDBHelper.insert(menu);



        menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-10", 0, 0, 7, 1344267898, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-10", 0, 0, 5, 1314367898, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-10", 0, 0, 11, 1314267298, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-10", 0, 0, 3, 1211767898, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-10", 0, 0, 8, 1212467898, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-10", 0, 0, 3, 1422367898, 1924454896);
        menuDBHelper.insert(menu);




        menu = new Menu("admin@tum.de", "Kartoffelcurry", "", 1,"2016-10-09", 0, 0, 3, 1344267818, 321527896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Dampfnudel mit Vanillesauce", "", 0,"2016-10-09", 0, 0, 4, 1314367818, 121568896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Bauerneintopf", "", 1,"2016-10-09", 0, 0, 14, 1314267218, 122563896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Gemüsegulasch", "", 1,"2016-10-09", 0, 0, 5, 1211767818, 123167896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Currywurst", "", 0,"2016-10-09", 0, 0, 11, 1212467818, 121554896);
        menuDBHelper.insert(menu);

        menu = new Menu("admin@tum.de", "Schnitzel", "", 0,"2016-10-09", 0, 0, 6, 1422367818, 1924454896);
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
