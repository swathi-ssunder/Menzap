package diy.net.menzap.fragments;

/**
 * Created by swathissunder on 03/10/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import diy.net.menzap.R;

public class StatsFragment extends Fragment {

    public StatsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button crowdDensityStats = (Button) getView().findViewById(R.id.stats1);
        crowdDensityStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.this.loadCrowdDensityStats();
            }
        });

        Button trendStats = (Button) getView().findViewById(R.id.stats2);
        trendStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.this.loadTrendStats();
            }
        });
    }

    private void loadCrowdDensityStats() {
        CrowdDensityFragment nextFrag = new CrowdDensityFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.layoutContainer, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void loadTrendStats() {
        TrendFragment nextFrag = new TrendFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.layoutContainer, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }
}