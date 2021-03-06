package diy.net.menzap.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import diy.net.menzap.vendor.DayAxisValueFormatter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import diy.net.menzap.R;
import diy.net.menzap.helper.TrackingDBHelper;

public class CrowdDensityFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SwipeRefreshLayout swipeLayout;
    private TrackingDBHelper trackingDBHelper;
    private JSONObject locationData;
    private BarChart mChart;
    private SeekBar mSeekBarX;
    private TextView tvX;
    protected Typeface mTfLight;

    public CrowdDensityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_crowd_density, container, false);

        this.trackingDBHelper = new TrackingDBHelper(getActivity());
        SQLiteDatabase db = this.trackingDBHelper.getReadableDatabase();
        this.trackingDBHelper.onCreate(db);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                CrowdDensityFragment.this.refreshView(mSeekBarX.getProgress());
            }
        });

        tvX = (TextView) view.findViewById(R.id.tvXMax);

        mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mChart = (BarChart) view.findViewById(R.id.barChart);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDrawGridBackground(false);

        mSeekBarX.setProgress(5);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(0f);

        mChart.getAxisRight().setEnabled(false);
        // add a nice and smooth animation
        mChart.animateY(2500);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshView(mSeekBarX.getProgress());
    }

    private void refreshView(int days) {
        Date date = new Date();
        long toTs = date.getTime();
        long fromTs = toTs - days * 24 * 3600 * 1000;

        this.locationData = this.trackingDBHelper.getByLocation(fromTs, toTs);

        this.setData(days);
        mChart.invalidate();

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            this.refreshView(mSeekBarX.getProgress());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress()));
        this.refreshView(mSeekBarX.getProgress());
    }

    private void setData(int count) {

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_YEAR) - count;

        float start = (float)day;
        float end = (float)day + count;
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x3 dataset
        float barWidth = 0.3f; // x3 dataset
        int val1 = 0, val2 = 0, val3 = 0;

        mChart.getXAxis().setAxisMinimum(start);
        mChart.getXAxis().setAxisMaximum(end);

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();
        ArrayList<BarEntry> yVals3 = new ArrayList<>();

        for (int i = (int) start; i <= end; i++) {
            ArrayList dayData = (ArrayList) this.locationData.opt(String.valueOf(i + 1));
            if (dayData == null) {
                val1 = val2 = val3 = 0;
            } else {
                val1 = (int) ((JSONObject) (dayData.get(0))).opt("USER_COUNT");
                val2 = (int) ((JSONObject) (dayData.get(1))).opt("USER_COUNT");
                val3 = (int) ((JSONObject) (dayData.get(2))).opt("USER_COUNT");
            }

            yVals1.add(new BarEntry(i, val1));
            yVals2.add(new BarEntry(i, val2));
            yVals3.add(new BarEntry(i, val3));
        }

        BarDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create 3 datasets with different types
            set1 = new BarDataSet(yVals1, "Location 1");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Location 2");
            set2.setColor(Color.rgb(242, 247, 158));
            set3 = new BarDataSet(yVals3, "Location 3");
            set3.setColor(Color.rgb(164, 228, 251));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(dataSets);

            mChart.setData(data);
        }
        mChart.getBarData().setBarWidth(barWidth);
        mChart.groupBars(start, groupSpace, barSpace);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
}