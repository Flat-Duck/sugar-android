package ly.bithive.sugar.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.ChartXValueFormatter;
import ly.bithive.sugar.R;
import ly.bithive.sugar.util.Helper;
import ly.bithive.sugar.util.SqliteHelper;
import me.itangqi.waveloadingview.WaveLoadingView;
public class StatsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SqliteHelper sqliteHelper;
    private Float totalPercentage = 0f;
    private Float totalGlasses = 0f;
    TextView remainingIntake, targetIntake;
    ImageButton btnBack;
    LineChart chart;
    WaveLoadingView waterLevelView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

      //  chart = new LineChart(this);
        sharedPref = getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
        sqliteHelper = new SqliteHelper(this);


        btnBack = findViewById(R.id.btnBack);
        targetIntake = findViewById(R.id.targetIntake);
        remainingIntake = findViewById(R.id.remainingIntake);
        waterLevelView = findViewById(R.id.waterLevelView);
        chart = findViewById(R.id.chart);

        targetIntake.setText(sharedPref.getInt(COMMON.TOTAL_INTAKE, 0) + " ml");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int percentage = sqliteHelper.getIntook(Helper.getCurrentDate()) * 100 / sharedPref.getInt(COMMON.TOTAL_INTAKE, 0);
        waterLevelView.setCenterTitle(percentage+"%");
        waterLevelView.setProgressValue(percentage);
        int remaining = sharedPref.getInt(COMMON.TOTAL_INTAKE, 0) - sqliteHelper.getIntook(Helper.getCurrentDate());

        if (remaining > 0) {
            remainingIntake.setText(remaining + " ml");
        } else {
            remainingIntake.setText("0 ml");
        }

        drawChart();

    }

    private void drawChart() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> dateArray = new ArrayList<>();
        Cursor cursor = sqliteHelper.getAllStats();
        if (cursor.moveToFirst()) {

            int i = 0;
            while (i<cursor.getCount()){
                dateArray.add(cursor.getString(1));
                int percent = cursor.getInt(2) / cursor.getInt(3) * 100;
                totalPercentage += percent;
                totalGlasses += cursor.getInt(2);
                entries.add(new Entry(i, percent));
                cursor.moveToNext();
                i++;
            }

        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show();
        }

        if (!entries.isEmpty()) {

            chart.getDescription().setText("Water consumption chart ");
            //chart.description.isEnabled = false;
            chart.animateY(1000, Easing.Linear);
            chart.getViewPortHandler().setMaximumScaleX(1.5f);
            chart.getXAxis().setDrawGridLines(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
            chart.getXAxis().setGranularityEnabled(true);

            chart.getLegend().setEnabled(false);
            chart.fitScreen();
            chart.setAutoScaleMinMaxEnabled(true);
            chart.setScaleX(1f);
            chart.setPinchZoom(true);
            chart.setScaleXEnabled(true);
            chart.setScaleYEnabled(false);
            chart.getAxisLeft().setTextColor(Color.BLACK);
            chart.getXAxis().setTextColor(Color.BLACK);
            chart.getAxisLeft().setDrawAxisLine(false);
            chart.getXAxis().setDrawAxisLine(false);
            chart.setDrawMarkers(false);
            chart.getXAxis().setLabelCount(5);
            YAxis rightAxix = chart.getAxisRight();
            rightAxix.setDrawGridLines(false);
            rightAxix.setDrawZeroLine(false);
            rightAxix.setDrawAxisLine(false);
            rightAxix.setDrawLabels(false);

            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(2.5f);
            dataSet.setColor(ContextCompat.getColor(this, R.color.colorSecondaryDark));
            dataSet.setDrawFilled(true);
            dataSet.setFillDrawable(getDrawable(R.drawable.graph_fill_gradiant));
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);
            chart.getXAxis().setValueFormatter((new ChartXValueFormatter(dateArray)));
            chart.setData(lineData);
            chart.invalidate();

        }
    }
}