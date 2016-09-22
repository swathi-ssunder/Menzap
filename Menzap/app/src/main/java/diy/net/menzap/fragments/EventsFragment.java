package diy.net.menzap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.Date;

import diy.net.menzap.R;
import diy.net.menzap.activity.EventCreateActivity;
import diy.net.menzap.activity.EventDetailActivity;
import diy.net.menzap.adapter.EventAdapter;
import diy.net.menzap.model.Event;


public class EventsFragment extends ListFragment implements AdapterView.OnItemClickListener {
    String startDate = "21/09/2016";
    String endDate = "22/09/2016";

    Event[] events =  new Event[] {
            new Event("Hackathon Registration on 1st Floor", "Technical event", "First Floor Mensa Garching", startDate, endDate),
            new Event("Free Deserts near Ausgabe 2", "Promotion", "Ground Floor Mensa Garching", startDate, endDate),
            new Event("Mexican Food Festival starts tomorrow", "Experience the culture", "First Floor Mensa Garching", startDate, endDate)
    };

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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventAdapter adapter = new EventAdapter(getActivity(), R.layout.event, events);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT", events[position]);
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
        startActivity(intent);
    }
}
