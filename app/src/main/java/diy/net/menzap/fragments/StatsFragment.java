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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import diy.net.menzap.R;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.model.Menu;

import com.like.LikeButton;

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

        MenuDBHelper menuDBHelper = new MenuDBHelper(getContext());

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String today = dateFormat.format(cal.getTime());

        Menu menu = menuDBHelper.getTopItem(today);

        TextView textView = (TextView) view.findViewById(R.id.popularDish);
        LikeButton btnLike = (LikeButton) view.findViewById(R.id.btnLike);


        if (menu != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(menu.getName());
            btnLike.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            btnLike.setVisibility(View.GONE);
        }

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