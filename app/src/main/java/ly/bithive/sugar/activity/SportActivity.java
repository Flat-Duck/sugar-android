package ly.bithive.sugar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import ly.bithive.sugar.R;
import io.feeeei.circleseekbar.CircleSeekBar;
import ly.bithive.sugar.util.SqliteHelper;


public class SportActivity extends AppCompatActivity implements CircleSeekBar.OnSeekBarChangeListener {
    CircleSeekBar seekBar;
    Context context;
    TextView seekBarVal;
    Button saveBtn;
    private int year, month, day,sPeriod;
    private String sTime, sDate, sType;
    private SqliteHelper sqliteHelper;
    ImageButton btnSportTime, btnSportDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        context = this;
        seekBarVal = findViewById(R.id.seekBarVal);
        seekBar = findViewById(R.id.seekBar);
        saveBtn = findViewById(R.id.sportSave);
        sqliteHelper = new SqliteHelper(this);
        btnSportDate = findViewById(R.id.sportDateBtn);
        btnSportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
                month = calendar.get(android.icu.util.Calendar.MONTH);
                year = calendar.get(android.icu.util.Calendar.YEAR);

                DatePickerDialog mTimePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int thisYear, int thisMonth, int dayOfMonth) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        time.set(Calendar.MONTH, thisMonth);
                        time.set(Calendar.YEAR, thisYear);
                        sDate = String.format("%02d/%02d/%02d", thisYear, thisMonth, dayOfMonth);
                        Toast.makeText(getApplicationContext(), sTime, Toast.LENGTH_LONG).show();
                    }
                }, year, month, day);
                mTimePicker.setTitle("Select Sleeping Time");
                mTimePicker.show();
            }
        });
        btnSportTime = findViewById(R.id.sportTimeBtn);
        btnSportTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, selectedHour);
                        time.set(Calendar.MINUTE, selectedMinute);
                        sTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        Toast.makeText(getApplicationContext(), sTime, Toast.LENGTH_LONG).show();
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                mTimePicker.setTitle("Select Sleeping Time");
                mTimePicker.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sqliteHelper.addSport(sDate,sTime,sType,sPeriod);
                Toast.makeText(getApplicationContext(), "Saved OK", Toast.LENGTH_LONG).show();

            }
        });
        final Spinner spinner = findViewById(R.id.spinner);

        seekBar.setMaxProcess(600);
        seekBar.setCurProcess(0);
        seekBar.setOnSeekBarChangeListener(this);


        String[] data = {"جري", "سباحة", "مشي", "دراجة", "حصة لياقة", "نشاط خفيف", "نشاط شديد"};


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sType = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SportActivity.this, sType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onChanged(CircleSeekBar seekbar, int curValue) {
        seekBarVal.setText(curValue + " Minutes");
        sPeriod = curValue;
    }
}