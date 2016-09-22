package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.activity.EventDetailActivity;
import diy.net.menzap.activity.MenuCreateActivity;
import diy.net.menzap.adapter.MenuAdapter;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.model.Menu;


public class MenuFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<Menu> menus;

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
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshView();
    }

    private void refreshView() {
        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());
        Log.d("allevents", menuDBHelper.getAll().toString());

        this.menus = menuDBHelper.getAll();
        MenuAdapter adapter = new MenuAdapter(getActivity(), R.layout.menu, menus);

        setListAdapter(adapter);
        // TODO : Implement card for menus, no drill down
        //getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT", this.menus.get(position));
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton addButton = (FloatingActionButton) getView().findViewById(R.id.add);

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
}
