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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import diy.net.menzap.R;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.vendor.DayAxisValueFormatter;

public class TrendFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SwipeRefreshLayout swipeLayout;
    private JSONObject trendData;
    private LineChart mChart;
    private SeekBar mSeekBarX;
    private TextView tvX;
    protected Typeface mTfLight;

    public TrendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_trends, container, false);

        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());
        SQLiteDatabase db = menuDBHelper.getReadableDatabase();
        menuDBHelper.onCreate(db);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                TrendFragment.this.refreshView(mSeekBarX.getProgress());
            }
        });

        tvX = (TextView) view.findViewById(R.id.tvXMax);

        mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mChart = (LineChart) view.findViewById(R.id.lineChart);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);

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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String toDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -days);
        String fromDate = dateFormat.format(cal.getTime());

        MenuDBHelper menuDBHelper = new MenuDBHelper(getActivity());

        this.trendData = menuDBHelper.getLikeCountOverTime(fromDate, toDate);

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

    protected void setData(int count) {

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_YEAR) - count;

        float start = (float)day;
        float end = (float)day + count;
        int val;

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> sets = new ArrayList<>();

        Iterator<String> keys = this.trendData.keys();
        int dishIndex = 0;
        while(keys.hasNext()) {
            String dishName = keys.next();

            JSONObject dishData = (JSONObject) this.trendData.opt(dishName);

            for (int i = (int) start; i <= end; i++) {
                if (dishData == null) {
                    val = 0;
                } else {
                    try {
                        val = (int)(dishData.get(String.valueOf(i)));
                    } catch (JSONException e) {
                        val = 0;
                    }
                }
                yVals1.add(new Entry(i, val));
            }
            LineDataSet ds = new LineDataSet(yVals1, dishName);
            ds.setColor(ColorTemplate.VORDIPLOM_COLORS[dishIndex % 5]);
            ds.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[dishIndex % 5]);
            ds.setLineWidth(2.5f);
            ds.setCircleRadius(3f);
            sets.add(ds);
            dishIndex++;
        }

        LineData d = new LineData(sets);
        mChart.setData(d);
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
