package ly.bithive.sugar.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieEntry;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import ly.bithive.sugar.R;
import ly.bithive.sugar.ReportItem;
import ly.bithive.sugar.ReportListAdapter;
import ly.bithive.sugar.SportReportAdapter;
import ly.bithive.sugar.SportReportItem;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.SqliteHelper;


import static ly.bithive.sugar.util.COMMON.KEY_TIME;
import static ly.bithive.sugar.util.COMMON.KEY_TYPE;
import static ly.bithive.sugar.util.COMMON.KEY_DURATION;
import static ly.bithive.sugar.util.COMMON.WAKEUP_TIME;
import static ly.bithive.sugar.util.SqliteHelper.KEY_DATE;

public class ReportActivity extends AppCompatActivity {

   // private static final String TAG = ReportActivity.class.getSimpleName();
    Context context;
  //  PieChart pieChart;
SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        context = this;
        sqliteHelper = new SqliteHelper(context);
        ViewPager reportPager = findViewById(R.id.reportPager);
        DotsIndicator indicator = findViewById(R.id.indicator);
        ReportFragAdapter viewPagerAdapter = new ReportFragAdapter(getSupportFragmentManager());
        reportPager.setAdapter(viewPagerAdapter);
        indicator.setViewPager(reportPager);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // PrepareReportData();
    }

    private class ReportFragAdapter extends FragmentPagerAdapter {
        ReportFragAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new PieChartReport();
                case 2:
                    return new LineChartReport(context);
                case 0:
                default:
                    return new TextReport(context);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class TextReport extends Fragment {
        RecyclerView recyclerView;
        ReportListAdapter mAdapter;
        Context mContext;
        List<ReportItem> reportItems;

        TextReport(Context context) {
            this.mContext = context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_text_report, container, false);
            reportItems = new ArrayList<>();
            recyclerView = view.findViewById(R.id.rvReport);
            mAdapter = new ReportListAdapter(reportItems);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            PrepareReportData();

            return view;

        }

        private void PrepareReportData() {
            ReportItem reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            reportItem = new ReportItem("03/06/2019", "10", "11", "12", "13", "14", "15", "16", "17");
            reportItems.add(reportItem);
            mAdapter.notifyDataSetChanged();

        }
    }

    public static class PieChartReport extends Fragment {
        PieChart pieChart;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pie_report, container, false);

            pieChart = view.findViewById(R.id.pieChart);



            pieChart.setDrawHoleEnabled(false);
            Description description = new Description();
            description.setEnabled(false);
            pieChart.setDescription(description);
            List<PieEntry> yValue = new ArrayList<>();


            yValue.add(new PieEntry(50,"مرتفع"));
            yValue.add(new PieEntry(30,"متالي"));
            yValue.add(new PieEntry(20,"منخفض"));

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.rgb(211,47, 47));
            colors.add(Color.rgb(56,142, 60));
            colors.add(Color.rgb(83,109, 254));
            colors.add(R.color.chartOk);
            colors.add(R.color.chartLo);


            PieDataSet set = new PieDataSet(yValue, "");
            set.setDrawValues(true);
            set.setValueTextSize(28);
           // set.setValueTe;
            set.setColors(colors);
            set.setValueTextColor(Color.BLACK);
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.invalidate(); // refresh





















//            pieChart.setCenterTextSize(64f);
//          //  pieChart.setHoleRadius(0f);
//            pieChart.setCenterTextSizePixels(48);
//           // pieChart.setTransparentCircleRadius(0f);
//           // pieChart.texts
//            drawChart();
//            pieChart.setUsePercentValues(true);
//            ArrayList<PieEntry> xvalues = new ArrayList<PieEntry>();
//            xvalues.add(new PieEntry(50.0f,"libya"));
//            xvalues.add(new PieEntry(28.2f, "Coventry"));
//            xvalues.add(new PieEntry(37.9f, "Manchester"));
//            PieDataSet dataSet = new PieDataSet(xvalues, "");
//            PieData data = new PieData(dataSet);
//            //pieChartColor(data,dataSet);
//            // In Percentage
//            data.setValueFormatter(new PercentFormatter());
//
//            pieChart.setData(data);
//            //pieChart.setDescription(().setText("ssss"));//iption.text = ""
//            pieChart.setDrawHoleEnabled(false);
//            data.setValueTextSize(13f);

            return view;
        }
        private void drawChart() {
//            ArrayList<Entry> entries = new ArrayList<>();
//            ArrayList<String> dateArray = new ArrayList<>();
//            Cursor cursor = sqliteHelper.getAllStats();
//            if (cursor.moveToFirst()) {
//
//                int i = 0;
//                while (i<cursor.getCount()){
//                    dateArray.add(cursor.getString(1));
//                    int percent = cursor.getInt(2) / cursor.getInt(3) * 100;
//                    totalPercentage += percent;
//                    totalGlasses += cursor.getInt(2);
//                    entries.add(new Entry(i, percent));
//                    cursor.moveToNext();
//                    i++;
//                }
//
//            } else {
//                Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show();
//            }
//
//            if (!entries.isEmpty()) {

               // pieChart.getDescription().setText("asdas");
//            ArrayList<PieEntry> NoOfEmp = new ArrayList<PieEntry>();
//
//            NoOfEmp.add(new PieEntry(20f, "Low", Color.BLUE));
//            NoOfEmp.add(new PieEntry(50f, "High"));
//            NoOfEmp.add(new PieEntry(30f, "OK"));
//            PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");
//            PieData data = new PieData(dataSet);
//            //data.setDataSet();
//            pieChart.setData(data);
//            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//            pieChart.animateXY(2000, 2000);

       //     }
        }
    }

    public static class LineChartReport extends Fragment {
        RecyclerView recyclerView;
        SportReportAdapter mAdapter;
        Context mContext;
        List<SportReportItem> reportItems;

        LineChartReport(Context context) {
            this.mContext = context;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sport_report, container, false);

             reportItems = new ArrayList<>();
            recyclerView = view.findViewById(R.id.rvSportReport);
            mAdapter = new SportReportAdapter(mContext,reportItems);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            PrepareSportReportData();


            return view;
        }

        private void PrepareSportReportData() {
            SqliteHelper  sqliteHelper = new SqliteHelper(getActivity().getApplicationContext());
            String type,date,time;
            int duration;
           // Cursor cursor = ;
           // for (cursor.)
                try (Cursor cursor = sqliteHelper.getAllSports()) {
                while (cursor.moveToNext()) {
                    if (cursor != null) {
                        int dateIndex = cursor.getColumnIndexOrThrow(KEY_DATE);
                        int typeIndex = cursor.getColumnIndexOrThrow(KEY_TYPE);
                        int durationIndex = cursor.getColumnIndexOrThrow(KEY_DURATION);
                        int timeIndex = cursor.getColumnIndexOrThrow(KEY_TIME);
                        date = cursor.getString(dateIndex);
                        type = cursor.getString(typeIndex);
                        duration = cursor.getInt(durationIndex);
                        time = cursor.getString(timeIndex);
                        SportReportItem sportReportItem = new SportReportItem(date,String.valueOf(duration),time,type);
                        reportItems.add(sportReportItem);
                    }
                }

                   // Cursor mCursor = db.rawQuery(selectQuery, whereArgs);

            }

        }
    }


}

