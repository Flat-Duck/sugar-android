package ly.bithive.sugar.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.math.RoundingMode;
import java.util.Calendar;

import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.R;
import ly.bithive.sugar.util.Helper;

public class InitUserInfoActivity extends AppCompatActivity {

    private int weight = 0;
    private int workTime = 0;
    private long wakeupTime;
    private long sleepingTime;
    ImageButton btnWakeUpTime,btnSleepTime;
    private SharedPreferences sharedPref;
    private boolean doubleBackToExitPressedOnce = false;
    TextInputLayout etWakeUpTime, etSleepTime, etWeight, etWorkTime;
    Button btnContinue;
    ConstraintLayout init_user_info_parent_layout;
    Context context;
    private int  mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_user_info);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        btnWakeUpTime = findViewById(R.id.btnWakeUpTime);
        btnSleepTime = findViewById(R.id.btnSleepTime);
        btnContinue = findViewById(R.id.btnContinue);
        etWakeUpTime = findViewById(R.id.etWakeUpTime);
        etSleepTime = findViewById(R.id.etSleepTime);
        etWeight = findViewById(R.id.etWeight);
        etWorkTime = findViewById(R.id.etWorkTime);
        init_user_info_parent_layout = findViewById(R.id.init_user_info_parent_layout);


        context = this;
        sharedPref = getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);

        wakeupTime = sharedPref.getLong(COMMON.WAKEUP_TIME, 1558323000000L);
        sleepingTime = sharedPref.getLong(COMMON.SLEEPING_TIME_KEY, 1558369800000L);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;

                imm.hideSoftInputFromWindow(init_user_info_parent_layout.getWindowToken(), 0);

                weight = Integer.parseInt(etWeight.getEditText().getText().toString());
                workTime = Integer.parseInt(etWorkTime.getEditText().getText().toString());
//                workTime = etWorkTime.editText!!.text.toString()


                if (TextUtils.isEmpty(String.valueOf(weight)))
                    Snackbar.make(view, "Please input your weight", Snackbar.LENGTH_SHORT).show();
                else if (weight > 200 || weight < 20)
                    Snackbar.make(view, "Please input a valid weight", Snackbar.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(String.valueOf(workTime)))
                    Snackbar.make(view, "Please input your workout time", Snackbar.LENGTH_SHORT).show();
                else if (workTime > 500 || workTime < 0)
                    Snackbar.make(view, "Please input a valid workout time", Snackbar.LENGTH_SHORT).show();
                else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(COMMON.WEIGHT_KEY, weight);
                    editor.putInt(COMMON.WORK_TIME_KEY, workTime);
                    editor.putLong(COMMON.WAKEUP_TIME, wakeupTime);
                    editor.putLong(COMMON.SLEEPING_TIME_KEY, sleepingTime);
                    editor.putBoolean(COMMON.FIRST_RUN_KEY, false);

                    double totalIntake = Helper.calculateIntake(weight, workTime);
                    DecimalFormat df = new DecimalFormat("#");
                    df.setRoundingMode(RoundingMode.CEILING.ordinal());
                    editor.putInt(COMMON.TOTAL_INTAKE, Integer.parseInt(df.format(totalIntake)));

                    editor.apply();
                    startActivity(new Intent(InitUserInfoActivity.this, MainActivity.class));
                    finish();
                }
            }
        });



//        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        setTime(hourOfDay,minute);
//                    }
//                }, mHour, mMinute, true);
//        timePickerDialog.show();
        btnWakeUpTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
                mHour = c.get(android.icu.util.Calendar.HOUR_OF_DAY);
                mMinute = c.get(android.icu.util.Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setWakeUpTime(selectedHour,selectedMinute);
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, selectedHour);
                        time.set(Calendar.MINUTE, selectedMinute);
                        wakeupTime = time.getTimeInMillis();
                        etWakeUpTime.getEditText().setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, mHour, mMinute, true);
                mTimePicker.setTitle("Select Wakeup Time");
                mTimePicker.show();
            }
        });


        btnSleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(sleepingTime);
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, selectedHour);
                        time.set(Calendar.MINUTE, selectedMinute);
                        sleepingTime = time.getTimeInMillis();
                        etSleepTime.getEditText().setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                mTimePicker.setTitle("Select Sleeping Time");
                mTimePicker.show();
            }
        });
    }

    private void setWakeUpTime(int selectedHour, int selectedMinute) {
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(R.id.init_user_info_parent_layout), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }

}
