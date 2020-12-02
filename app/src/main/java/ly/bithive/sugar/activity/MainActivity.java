package ly.bithive.sugar.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Button;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

import ly.bithive.sugar.util.SetupActions;
import ly.bithive.sugar.util.SqliteHelper;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.Helper;
import ly.bithive.sugar.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner measureSpinnerTime, measureSpinnerCure;
    TextInputEditText txtTime;
private long wakeupTime;
    Button addBtn;
    private int mHour, mMinute;
    TextInputLayout etInsulin, etGlycemia;
    private Context context;
    SetupActions setupActions;
    SqliteHelper sqliteHelper;
    ImageButton btnWakeUpTime,btnSleepTime;
    TextInputLayout etWakeUpTime;
    private int totalIntake = 0;
    private SharedPreferences sharedPref;
    private boolean doubleBackToExitPressedOnce = false;
    private Helper helper;
    ImageView btnUser;

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_shot_fragment, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        etWakeUpTime = view.findViewById(R.id.etWakeUpTime);
        measureSpinnerTime = view.findViewById(R.id.spPeriod);
        measureSpinnerCure = view.findViewById(R.id.spCure);
        etGlycemia = view.findViewById(R.id.etGlycemia);
        addBtn = view.findViewById(R.id.btnAdd);
        btnWakeUpTime = view.findViewById(R.id.btnWakeUpTime);
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

      //  etInsulin = view.findViewById(R.id.etInsulin);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int glycemia = Integer.parseInt(etGlycemia.getEditText().getText().toString());
                sqliteHelper.insertShot(Helper.getCurrentDate(), Helper.getCurrentTime(),
                        etGlycemia.getEditText().getText().toString(),
                       // etInsulin.getEditText().getText().toString(),
                        measureSpinnerTime.getSelectedItem().toString(),
                        measureSpinnerCure.getSelectedItem().toString());
                boolean exit = false;
                if (glycemia > COMMON.DANGER_HIGH) {
                    showAlertMsg(R.string.danger_label, R.string.h_danger_msg, "red");
                    exit = true;
                }
                if (glycemia < COMMON.DANGER_LOW) {
                    showAlertMsg(R.string.danger_label, R.string.l_danger_msg, "red");

                    exit = true;
                }
                if (!exit) {
                    if (measureSpinnerTime.getSelectedItem() == getResources().getString(R.string.after_eat)) {

                        if (glycemia < COMMON.BE_LOW_2) {
                            showAlertMsg(R.string.l2_label, R.string.l2_msg, "yellow");
                        } else if (glycemia < COMMON.BE_LOW_1) {
                            showAlertMsg(R.string.l1_label, R.string.l1_msg, "yellow");
                        } else if (glycemia > COMMON.BE_HIGH_1) {
                            showAlertMsg(R.string.h1_label, R.string.h1_msg, "yellow");
                        } else if (glycemia > COMMON.BE_HIGH_2) {
                            showAlertMsg(R.string.h2_label, R.string.h2_msg, "yellow");
                        } else if (glycemia > COMMON.BE_HIGH_3) {
                            showAlertMsg(R.string.h3_label, R.string.h3_msg, "red");
                        } else {
                            showAlertMsg(R.string.normal_label, R.string.normal_msg, "green");
                        }
                    } else if (measureSpinnerTime.getSelectedItem() == getResources().getString(R.string.before_eat)) {
                        if (glycemia < COMMON.AE_LOW_2) {
                            showAlertMsg(R.string.l2_label, R.string.l2_msg, "yellow");
                        } else if (glycemia < COMMON.AE_LOW_1) {
                            showAlertMsg(R.string.l1_label, R.string.l1_msg, "yellow");
                        } else if (glycemia > COMMON.AE_HIGH_1) {
                            showAlertMsg(R.string.h1_label, R.string.h1_msg, "yellow");
                        } else if (glycemia > COMMON.AE_HIGH_2) {
                            showAlertMsg(R.string.h2_label, R.string.h2_msg, "yellow");
                        } else if (glycemia > COMMON.AE_HIGH_3) {
                            showAlertMsg(R.string.h3_label, R.string.h3_msg, "red");
                        } else {
                            showAlertMsg(R.string.normal_label, R.string.normal_msg, "green");
                        }
                    } else if (measureSpinnerTime.getSelectedItem() == getResources().getString(R.string.before_sleep)) {
                        if (glycemia < COMMON.BS_LOW_1) {
                            showAlertMsg(R.string.l2_label, R.string.l2_msg, "yellow");
                        } else if (glycemia < COMMON.BS_LOW_2) {
                            showAlertMsg(R.string.l1_label, R.string.l1_msg, "yellow");
                        } else if (glycemia > COMMON.BS_HIGH_1) {
                            showAlertMsg(R.string.h1_label, R.string.h1_msg, "yellow");
                        } else if (glycemia > COMMON.BS_HIGH_2) {
                            showAlertMsg(R.string.h2_label, R.string.h2_msg, "yellow");
                        } else if (glycemia > COMMON.BS_HIGH_3) {
                            showAlertMsg(R.string.h3_label, R.string.h3_msg, "red");
                        } else {
                            showAlertMsg(R.string.normal_label, R.string.normal_msg, "green");
                        }
                    }
                }
                dialog.dismiss();
            }
        });

        measureSpinnerTime.setAdapter(setupActions.loadPeriodsArray());
        measureSpinnerCure.setAdapter(setupActions.loadCureArray());
        dialog.setContentView(view);
        dialog.show();
    }
    private void setWakeUpTime(int selectedHour, int selectedMinute) {
    }
    private void showAlertMsg(int ttl, int msg, String color) {
        int bg;
        switch (color) {
            case "green":
                bg = R.drawable.alert_green_bg;
                break;
            case "yellow":
                bg = R.drawable.alert_yelow_bg;
                break;
            case "red":
                bg = R.drawable.alert_red_bg;
                break;
            default:
                bg = R.drawable.alert_white_bg;
        }

        final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setBackground(getDrawable(bg)).setNeutralButton("موافق", null);
        dialogBuilder.setTitle(ttl);
        dialogBuilder.setMessage(msg);
        dialogBuilder.show();

    }

    private void setTime(int hourOfDay, int minute) {
        txtTime.setText(hourOfDay + getString(R.string.coma) + minute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActions = new SetupActions(this);
        sqliteHelper = new SqliteHelper(this);
        btnUser = findViewById(R.id.btnProfile);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        helper = new Helper();
        context = this;
     //   Button test_button = (Button) findViewById(R.id.button_test);


//        test_button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



        sharedPref = getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
        totalIntake = sharedPref.getInt(COMMON.TOTAL_INTAKE, 0);

        if (sharedPref.getBoolean(COMMON.FIRST_RUN_KEY, true)) {
            startActivity(new Intent(MainActivity.this, WalkThroughActivity.class));
            finish();
        } else if (totalIntake <= 0) {
            startActivity(new Intent(MainActivity.this, InitUserInfoActivity.class));
            finish();
        }
        //tvSelectedItem = findViewById(R.id.textView7);

        CardView cvMeasure = findViewById(R.id.cvMeasure);
        cvMeasure.setOnClickListener(this);
        CardView cvReport = findViewById(R.id.CVReport);
        cvReport.setOnClickListener(this);
        CardView cvWeight = findViewById(R.id.cvWeight);
        cvWeight.setOnClickListener(this);
        CardView cvWater = findViewById(R.id.cvWater);
        cvWater.setOnClickListener(this);
        CardView cvSport = findViewById(R.id.cvSport);
        cvSport.setOnClickListener(this);
        CardView cvFood = findViewById(R.id.cvFood);
        cvFood.setOnClickListener(this);
        CardView cvMessaging = findViewById(R.id.CVMessaging);
        cvMessaging.setOnClickListener(this);
        CardView cvAdvice = findViewById(R.id.cvAdvice);
        cvAdvice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvMeasure:
                showBottomSheetDialog();
                break;
            case R.id.cvWater:
                showWaterActivity();
                break;
            case R.id.cvSport:
                showStatActivity();
                break;
            case R.id.cvFood:
                showFoodActivity();
                break;
            case R.id.CVReport:
                showReportActivity();
                break;
            case R.id.cvWeight:
                showWeightActivity();
                break;
            case R.id.CVMessaging:
                showMessageActivity();
                break;
            case R.id.cvAdvice:
                showAdviceActivity();
                break;
        }
    }

    private void showWeightActivity() {
        startActivity(new Intent(MainActivity.this, BmiActivity.class));
    }

    private void showReportActivity() {
        startActivity(new Intent(MainActivity.this, ReportActivity.class));
    }

    private void showWaterActivity() {
        startActivity(new Intent(MainActivity.this, WaterActivity.class));
    }

    private void showFoodActivity() {
        startActivity(new Intent(MainActivity.this, FoodActivity.class));
    }

    private void showStatActivity() {
       // startActivity(new Intent(MainActivity.this, StatsActivity.class));
        showSportsAlert();
    }
    private void showMessageActivity() {
        startActivity(new Intent(MainActivity.this, MessagingActivity.class));
    }

    private void showAdviceActivity() {
        showAdviceAlert();
    }

    private void showAdviceAlert() {
        //Create LinearLayout Dynamically
        LinearLayout layout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Setup Layout Attributes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Add Views to the layout
        String[] advices = { "لاهتمام بنظافة القدمين يوميًا وخصوصًا بين الأصابع لمنع الجراثيم والبكتيريا التى تؤدى إلى بتر القدمين، مع عدم المشى حافيا",
                "الحد من تناول الأطعمة الدسمة التى تحتوى على نسبة دهون عالية منعا للإصابة بالسمنة.",
                "الابتعاد عن الحلويات واستبدالها بالعصائر الطبيعية.",
                "الإكثار فى تناول الخضراوات والفواكه، حيث إنها تساعد على تعزيز صحة الجسم وحمايته من الأمراض.",
                "مراقبة مستوى السكر كل يوم مع اتباع نظام غذائى صحى. ",
                "يجب أن يهتم مريض السكر بوجبة الإفطار، لتجنب الإصابة بغيبوبة السكر ومضاعفاتها. ",
                "تحديد مواعيد ثابتة للطعام على أن تكون خمس وجبات  على مدار اليوم بكميات قليلة ، وغنيه بالعناصر الغذائية المختلفة.",
                 "ممارسة النشاط البدنى والرياضة سواء من خلال المشى والحركة والسباحة  أو التمارين الرياضية فى الجيم.",
                 "الابتعاد عن التدخين، حيث أنه يؤثر على صحة القلب والأوعية الدموية.",
                 "تجنب المشروبات الغازية والكحولية نظرا لاحتوائها على نسبة عالية من السكريات والمواد الملونة  التى تضر الصحة.",
                 "الإكثار فى شرب المياه على إلا تقل عن 2 لتر مياه فى اليوم لتحسين وظائف الجسم.",
                 "النوم عدد ساعات كافيه لتجنب الأرق والتوتر.",
                 "الابتعاد عن الضغوط النفسية التى تؤثر على الصحة وتحديد يوم فى الأسبوع للتنزه أو زيارة الأقارب والأصدقاء.",
                 "الاهتمام بصحة الاسنان واللثة للكشف على أى مشاكل أخرى.",
                 "زيارة طبيب العيون باستمرار لتجنب الاصابة باى مضاعفات للعين نتيجة مرض السكر.",
                 "يجب توفير جهاز قياس السكر فى الأماكن التى يتواجد فيها المريض سواء المنزل أو العمل."};
        for(int i =0;i< advices.length; i++) {
            TextView textview =  new TextView(this);
            String advice = String.valueOf(Html.fromHtml("<li>"+advices[i]+"</li>",1));
            textview.setText(advice);
            layout.addView(textview);
        }
        scrollView.addView(layout);
        //Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Give the Dialog a Title
        builder.setTitle(R.string.general_advices);
        //Set the Dynamically created layout as the Dialogs view
        builder.setView(scrollView);
        //Add Dialog button that will just close the Dialog
        builder.setNeutralButton(R.string.ok, null);
        //Show the custom AlertDialog
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showSportsAlert() {

        //Create LinearLayout Dynamically
        LinearLayout layout = new LinearLayout(this);

        //Setup Layout Attributes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        String[] sportAdvices = getResources().getStringArray(R.array.sport_alert_list);


        for(int i =0;i< sportAdvices.length; i++) {
            TextView textview =  new TextView(this);
            String advice = String.valueOf(Html.fromHtml("<li>"+sportAdvices[i]+"</li>",1));
            textview.setText(advice);
            layout.addView(textview);
        }

        //Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Give the Dialog a Title
        builder.setTitle(getString(R.string.sport_alert_title));

        //Set the Dynamically created layout as the Dialogs view
        builder.setView(layout);

        //Add Dialog button that will just close the Dialog
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, SportActivity.class));
            }
        });

        //Show the custom AlertDialog
        AlertDialog alert = builder.create();
        alert.show();

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