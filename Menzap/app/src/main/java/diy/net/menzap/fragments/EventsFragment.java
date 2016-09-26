package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.content.Intent;
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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import diy.net.menzap.R;
import diy.net.menzap.activity.EventCreateActivity;
import diy.net.menzap.activity.EventDetailActivity;
import diy.net.menzap.adapter.EventAdapter;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.EventDBHelper;
import diy.net.menzap.model.Event;


public class EventsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<Event> events;
    private SwipeRefreshLayout swipeLayout;
    private EventAdapter eventAdapter;
    private EventDBHelper eventDBHelper;

    public EventsFragment() {
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

        this.eventDBHelper = new EventDBHelper(getActivity());
        SQLiteDatabase db = this.eventDBHelper.getReadableDatabase();
        this.eventDBHelper.onCreate(db);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                EventsFragment.this.refreshView();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshView();
    }

    public void refreshView() {
        Log.d("allevents", this.eventDBHelper.getAll().toString());

        this.events = this.eventDBHelper.getAll();
        this.eventAdapter = new EventAdapter(getActivity(), R.layout.event, this.events);

        this.handleNotification(this.events);

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);

        setListAdapter(this.eventAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT", this.events.get(position));
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton addButton = (FloatingActionButton) getView().findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsFragment.this.addEvent();
            }
        });
    }

    protected void addEvent() {
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            this.refreshView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleNotification(ArrayList<Event> events) {
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Date dateObj = new Date();
        String today = dateFormat.format(dateObj);
        for(Event event: events) {
            if((event.getIsInterested() == 1) && (today.equals(event.getFromDate()) || today.equals(event.getToDate()))) {
                DataHolder.getInstance().getNotificationHelper().notifyForEvent(event, true);
            }
        }
    }
}
