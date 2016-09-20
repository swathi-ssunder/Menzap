package diy.net.menzap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import diy.net.menzap.R;
import diy.net.menzap.activity.EventDetailActivity;


public class EventsFragment extends ListFragment implements AdapterView.OnItemClickListener {
    String[] events =  new String[] { "Hackathon Registration on 1st Floor", "Free Deserts near Ausgabe 2", "Mexican Food Festival starts tomorow" };
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, events);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("eventName", events[position]);
        intent.putExtra("startTime", "12:00 PM");
        intent.putExtra("endTime", "02:00 PM");
        intent.putExtra("location", "First Floor Mensa Garching");
        startActivity(intent);
    }

}
