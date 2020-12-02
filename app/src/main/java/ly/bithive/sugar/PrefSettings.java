package ly.bithive.sugar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.math.RoundingMode;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import ly.bithive.sugar.activity.MainActivity;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.SqliteHelper;
import ly.bithive.sugar.util.AlarmHelper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class PrefSettings extends BottomSheetDialogFragment {

    private SharedPreferences sharedPref;
    private String weight = "";
    private String workTime = "";
    private String customTarget = "";
    private Long wakeupTime = 0L;
    private Long sleepingTime = 0L;
    private String notificMsg = "";
    private int notificFrequency = 0;
    private String currentToneUri = "";
    Context context;
    RadioRealButtonGroup radioNotificItervel;
    TextInputLayout etWeight, etWorkTime, etTarget, etNotificationText, etRingtone, etWakeUpTime, etSleepTime;
Button btnUpdate;
    public PrefSettings(Context mCtx) {
        this.context = mCtx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {

            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            currentToneUri = uri.toString();
            sharedPref.edit().putString(COMMON.NOTIFICATION_TONE_URI_KEY, currentToneUri).apply();
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            etRingtone.getEditText().setText(ringtone.getTitle(context));

        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = context.getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
        etWeight.getEditText().setText(sharedPref.getInt(COMMON.WEIGHT_KEY, 0));
        etWorkTime.getEditText().setText(sharedPref.getInt(COMMON.WORK_TIME_KEY, 0));
        etTarget.getEditText().setText(sharedPref.getInt(COMMON.TOTAL_INTAKE, 0));
        etNotificationText.getEditText().setText(sharedPref.getString(COMMON.NOTIFICATION_MSG_KEY, "Hey... Lets drink some water...."));
        currentToneUri = sharedPref.getString(COMMON.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
        etRingtone.getEditText().setText(RingtoneManager.getRingtone(context, Uri.parse(currentToneUri)).getTitle(context));

        radioNotificItervel.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                switch (position) {
                    case 0:
                        notificFrequency = 30;
                    case 1:
                        notificFrequency = 45;
                    case 2:
                        notificFrequency = 60;
                }
            }
        });

        notificFrequency = sharedPref.getInt(COMMON.NOTIFICATION_FREQUENCY_KEY, 30);
        switch (notificFrequency) {
            case 30:
                radioNotificItervel.setPosition(0);
                break;
            case 45:
                radioNotificItervel.setPosition(1);
                break;
            case 60:
                radioNotificItervel.setPosition(2);
                break;
            default: {
                notificFrequency = 30;
                radioNotificItervel.setPosition(0);
                break;
            }
        }


        etRingtone.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(
                        RingtoneManager.EXTRA_RINGTONE_TITLE,
                        "Select ringtone for notifications:"
                );
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentToneUri);
                startActivityForResult(intent, 999);

            }
        });
        wakeupTime = sharedPref.getLong(COMMON.WAKEUP_TIME, 1558323000000L);
        sleepingTime = sharedPref.getLong(COMMON.SLEEPING_TIME_KEY, 1558369800000L);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(wakeupTime);
        etWakeUpTime.getEditText().setText(String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
        cal.setTimeInMillis(sleepingTime);
        etSleepTime.getEditText().setText(String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));

        etWakeUpTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(wakeupTime);

                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, selectedHour);
                        time.set(Calendar.MINUTE, selectedMinute);
                        wakeupTime = time.getTimeInMillis();
                        etWakeUpTime.getEditText().setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                mTimePicker.setTitle("Select Wakeup Time");
                mTimePicker.show();
            }
        });


        etSleepTime.getEditText().setOnClickListener(new View.OnClickListener() {
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
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                mTimePicker.setTitle("Select Sleeping Time");
                mTimePicker.show();

            }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            int currentTarget = sharedPref.getInt(COMMON.TOTAL_INTAKE, 0);

            weight = etWeight.getEditText().getText().toString();
            workTime = etWorkTime.getEditText().getText().toString();
            notificMsg = etNotificationText.getEditText().getText().toString();
            customTarget = etTarget.getEditText().getText().toString();

                if(TextUtils.isEmpty(notificMsg)){ Toast.makeText(context, "Please a notification message", Toast.LENGTH_SHORT).show();}
                else if(notificFrequency == 0)Toast.makeText(context, "Please select a notification frequency", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(weight))Toast.makeText(context, "Please input your weight", Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(weight) > 200 || Integer.parseInt(weight) < 20)Toast.makeText(context, "Please input a valid weight", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(workTime))Toast.makeText(context, "Please input your workout time", Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(workTime) > 500 || Integer.parseInt(workTime) < 0) Toast.makeText(context, "Please input a valid workout time", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(customTarget))Toast.makeText(context, "Please input your custom target", Toast.LENGTH_SHORT).show();
        else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(COMMON.WEIGHT_KEY, Integer.parseInt(weight));
                    editor.putInt(COMMON.WORK_TIME_KEY, Integer.parseInt(workTime));
                    editor.putLong(COMMON.WAKEUP_TIME, wakeupTime);
                    editor.putLong(COMMON.SLEEPING_TIME_KEY, sleepingTime);
                    editor.putString(COMMON.NOTIFICATION_MSG_KEY, notificMsg);
                    editor.putInt(COMMON.NOTIFICATION_FREQUENCY_KEY, notificFrequency);

                    SqliteHelper sqliteHelper = new SqliteHelper(context);

                    if (currentTarget != Integer.parseInt(customTarget)) {
                        editor.putInt(COMMON.TOTAL_INTAKE, Integer.parseInt(customTarget));

                        sqliteHelper.updateTotalIntake(COMMON.getCurrentDate() , Integer.parseInt(customTarget));
                    } else {
                        int totalIntake = Integer.parseInt(String.valueOf(COMMON.calculateIntake(Integer.parseInt(weight), Integer.parseInt(workTime))));
                        DecimalFormat df = new DecimalFormat("#");
                        df.setRoundingMode(1);
                        editor.putInt(COMMON.TOTAL_INTAKE, Integer.parseInt(df.format(totalIntake)));

                        sqliteHelper.updateTotalIntake(COMMON.getCurrentDate(), Integer.parseInt(df.format(totalIntake)));
                    }

                    editor.apply();

                    Toast.makeText(context, "Values updated successfully", Toast.LENGTH_SHORT).show();
                    AlarmHelper alarmHelper = new AlarmHelper();
                    alarmHelper.cancelAlarm(context);
                    alarmHelper.setAlarm(context, (long) sharedPref.getInt(COMMON.NOTIFICATION_FREQUENCY_KEY, 30));
                    dismiss();
                    MainActivity activity = null;
                }
            }
        });
    }
}