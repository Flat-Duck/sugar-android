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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ly.bithive.sugar.R;
import ly.bithive.sugar.ReportItem;
import ly.bithive.sugar.ReportListAdapter;
import ly.bithive.sugar.SportReportAdapter;
import ly.bithive.sugar.SportReportItem;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.SqliteHelper;


import static ly.bithive.sugar.util.COMMON.BE_HIGH_1;
import static ly.bithive.sugar.util.COMMON.BE_LOW_1;
import static ly.bithive.sugar.util.COMMON.KEY_TIME;
import static ly.bithive.sugar.util.COMMON.KEY_TYPE;
import static ly.bithive.sugar.util.COMMON.KEY_DURATION;
import static ly.bithive.sugar.util.COMMON.WAKEUP_TIME;
import static ly.bithive.sugar.util.SqliteHelper.KEY_CURE;
import static ly.bithive.sugar.util.SqliteHelper.KEY_DATE;
import static ly.bithive.sugar.util.SqliteHelper.KEY_GLYCEMIA;
import static ly.bithive.sugar.util.SqliteHelper.KEY_PERIOD;

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
                    return new PieChartReport(context);
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
        SqliteHelper sqliteHelper;

        TextReport(Context context) {
            this.mContext = context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_text_report, container, false);
            reportItems = new ArrayList<>();
            recyclerView = view.findViewById(R.id.rvReport);
            mAdapter = new ReportListAdapter(reportItems);
            sqliteHelper = new SqliteHelper(mContext);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            PrepareReportData();

            return view;

        }

        private void PrepareReportData() {

            String value, period, time, dat, cure;


            try (Cursor cursor = sqliteHelper.getAllAnalysis()) {
                while (cursor.moveToNext()) {
                    if (cursor != null) {
                        int dateIndex = cursor.getColumnIndexOrThrow(KEY_DATE);
                        int timeIndex = cursor.getColumnIndexOrThrow(KEY_TIME);
                        int periodIndex = cursor.getColumnIndexOrThrow(KEY_PERIOD);
                        int valueIndex = cursor.getColumnIndexOrThrow(KEY_GLYCEMIA);
                        int cureIndex = cursor.getColumnIndexOrThrow(KEY_CURE);
                        dat = cursor.getString(dateIndex);
                        period = cursor.getString(periodIndex);
                        value = cursor.getString(valueIndex);
                        time = cursor.getString(timeIndex);
                        cure = cursor.getString(cureIndex);
                        ReportItem reportItem = new ReportItem(dat+":"+time, period, value, cure);
                        reportItems.add(reportItem);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

        public static class PieChartReport extends Fragment {
            PieChart pieChart;
            Context mContext;
            List<PieEntry> yValue;
            SqliteHelper sqliteHelper;
            Spinner spinner;

            PieChartReport(Context context) {
                this.mContext = context;
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                sqliteHelper = new SqliteHelper(mContext);
                View view = inflater.inflate(R.layout.fragment_pie_report, container, false);
                pieChart = view.findViewById(R.id.pieChart);
                pieChart.setDrawHoleEnabled(false);
                Description description = new Description();
                description.setEnabled(false);
                pieChart.setDescription(description);
                spinner = view.findViewById(R.id.pieSpinner);

                String[] data = {"اسبوع", "اسبوعين"};

                ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item_selected, data);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            calculataPie(7);

                        } else {
                            calculataPie(15);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                return view;
            }

            private void drawPieChart(double i, double i1, double i2) {

                yValue = new ArrayList<>();

                yValue.add(new PieEntry((float) i, "مرتفع"));
                yValue.add(new PieEntry((float) i1, "متالي"));
                yValue.add(new PieEntry((float) i2, "منخفض"));

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.rgb(211, 47, 47));
                colors.add(Color.rgb(56, 142, 60));
                colors.add(Color.rgb(83, 109, 254));
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

            }


            private void calculataPie(int i) {

                JSONArray reportItems = new JSONArray();
                int hi = 0, low = 0, mid = 0;

                String value;
                int limit = (i * 3);
                int counter = 0;
                Log.d("Limit", limit + "XXX");
                List<Integer> values = new ArrayList<Integer>();
                try (Cursor cursor = sqliteHelper.getAllAnalysis()) {
                    while (cursor.moveToNext()) {
                        if (cursor != null) {
                            if (counter == limit) {

                                break;

                            } else {

                                int valueIndex = cursor.getColumnIndexOrThrow(KEY_GLYCEMIA);
                                value = cursor.getString(valueIndex);
                                values.add(Integer.parseInt(value));
                                Log.d("Val", value);
                                JSONObject analysis = new JSONObject();
                                try {
                                    analysis.put(KEY_GLYCEMIA, value);
                                    reportItems.put(analysis);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                int factor = values.size();
                for (int x : values) {
                    if (x >= BE_HIGH_1) {
                        hi++;
                        Log.d("HI", hi + "");
                    } else if (x <= BE_LOW_1) {
                        low++;
                        Log.d("low", low + "");
                    } else {
                        mid++;
                        Log.d("mid", mid + "");
                    }
                }

                Log.d("FACTOR", factor + "");
                double LP = (low / Double.parseDouble(String.valueOf(factor))) * 100;
                double HP = (hi / Double.parseDouble(String.valueOf(factor))) * 100;
                double MP = (mid / Double.parseDouble(String.valueOf(factor))) * 100;

                Log.d("LOWP", LP + "");
                Log.d("MIDP", MP + "");
                Log.d("HIP", HP + "");
                drawPieChart(HP, MP, LP);
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
                mAdapter = new SportReportAdapter(mContext, reportItems);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                PrepareSportReportData();


                return view;
            }

            private void PrepareSportReportData() {
                SqliteHelper sqliteHelper = new SqliteHelper(getActivity().getApplicationContext());
                String type, date, time;
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
                            SportReportItem sportReportItem = new SportReportItem(date, String.valueOf(duration), time, type);
                            reportItems.add(sportReportItem);
                        }
                    }

                    // Cursor mCursor = db.rawQuery(selectQuery, whereArgs);

                }

            }
        }


    }

