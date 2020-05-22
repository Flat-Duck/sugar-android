package ly.bithive.sugar.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import ly.bithive.sugar.util.AlarmHelper;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.R;
import ly.bithive.sugar.util.Helper;
import ly.bithive.sugar.util.SqliteHelper;
import params.com.stepprogressview.StepProgressView;

public class WaterActivity extends AppCompatActivity {
    ImageButton btnNotific;
    FloatingActionButton fabAdd,btnStats;
    //Button btnStats;
    ImageView btnMenu,btnBack;
    LinearLayout op50ml, op100ml, op150ml, op200ml, op250ml, opCustom;
    TextView tvCustom;
    private int totalIntake = 0;
    private int inTook = 0;
    TextView tvIntook, tvTotalIntake;
    StepProgressView intakeProgress;
    private SharedPreferences sharedPref;
    private SqliteHelper sqliteHelper;
    private String dateNow;
    private Boolean notifyStatus = false;
    private int selectedOption = 0;
    private Snackbar snackbar = null;
    Helper helper;
    Context context;
    ConstraintLayout constraintLayout;
    CardView cardView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        context = this;

        cardView = findViewById(R.id.cardView);
        intakeProgress = findViewById(R.id.intakeProgress);
        tvIntook = findViewById(R.id.tvIntook);
        tvTotalIntake = findViewById(R.id.tvTotalIntake);
        fabAdd = findViewById(R.id.fabAdd);
        btnNotific = findViewById(R.id.btnNotific);
        btnStats = findViewById(R.id.btnStats);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        op50ml = findViewById(R.id.op50ml);
        op100ml = findViewById(R.id.op100ml);
        op150ml = findViewById(R.id.op150ml);
        op200ml = findViewById(R.id.op200ml);
        op250ml = findViewById(R.id.op250ml);
        opCustom = findViewById(R.id.opCustom);
        tvCustom = findViewById(R.id.tvCustom);

        constraintLayout = findViewById(R.id.main_activity_parent);

        helper = new Helper();
        sharedPref = getSharedPreferences(COMMON.USERS_SHARED_PREF, COMMON.PRIVATE_MODE);
          sqliteHelper = new SqliteHelper(context);
        totalIntake = sharedPref.getInt(COMMON.TOTAL_INTAKE, 0);

        if (sharedPref.getBoolean(COMMON.FIRST_RUN_KEY, true)) {
            startActivity(new Intent(WaterActivity.this, WalkThroughActivity.class));
            finish();
        } else if (totalIntake <= 0) {
            startActivity(new Intent(WaterActivity.this, InitUserInfoActivity.class));
            finish();
        }

        dateNow = Helper.getCurrentDate();
    }

    void updateValues() {
        totalIntake = sharedPref.getInt(COMMON.TOTAL_INTAKE, 0);
        inTook = sqliteHelper.getIntook(dateNow);
        setWaterLevel(inTook, totalIntake);
    }

    @SuppressLint("SetTextI18n")
    private void setWaterLevel(int inTook, int totalIntake) {

        YoYo.with(Techniques.SlideInDown).duration(500).playOn(tvIntook);
        tvIntook.setText(String.valueOf(inTook));
        tvTotalIntake.setText(" / " + totalIntake+" ml");
        int progress = ((inTook / totalIntake) * 100);
        YoYo.with(Techniques.Pulse).duration(500).playOn(intakeProgress);
        intakeProgress.setCurrentProgress(progress);
        if ((inTook * 100 / totalIntake) > 140) {
            Snackbar.make(constraintLayout, "You achieved the goal", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onStart() {
        super.onStart();
        final Resources.Theme theme = this.getTheme();
        final TypedValue outValue = new TypedValue();
        theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true);

        notifyStatus = sharedPref.getBoolean(COMMON.NOTIFICATION_STATUS_KEY, true);
        final AlarmHelper alarm = new AlarmHelper();
        if (!alarm.checkAlarm(context) && notifyStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell));
            alarm.setAlarm(context, sharedPref.getLong(COMMON.NOTIFICATION_FREQUENCY_KEY, 30));
        }

        if (notifyStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell));
        } else {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell_disabled));
        }

        sqliteHelper.addAll(dateNow, 0, totalIntake);

        updateValues();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedOption != 0) {
                    if ((inTook * 100 / totalIntake) <= 140) {
                        if (sqliteHelper.addIntook(dateNow, selectedOption) > 0) {
                            inTook += selectedOption;
                            setWaterLevel(inTook, totalIntake);
                            Snackbar.make(view, "Your water intake was saved...!!", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(view, "You already achieved the goal", Snackbar.LENGTH_SHORT).show();
                    }
                    selectedOption = 0;
                    tvCustom.setText("Custom");
                    op50ml.setBackground(getDrawable(outValue.resourceId));
                    op100ml.setBackground(getDrawable(outValue.resourceId));
                    op150ml.setBackground(getDrawable(outValue.resourceId));
                    op200ml.setBackground(getDrawable(outValue.resourceId));
                    op250ml.setBackground(getDrawable(outValue.resourceId));
                    opCustom.setBackground(getDrawable(outValue.resourceId));
                } else {
                    YoYo.with(Techniques.Shake).duration(700).playOn(cardView);
                    Snackbar.make(view, "Please select an option", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnNotific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyStatus = !notifyStatus;
                sharedPref.edit().putBoolean(COMMON.NOTIFICATION_STATUS_KEY, notifyStatus).apply();
                if (notifyStatus) {
                    btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell));
                    Snackbar.make(view, "Notification Enabled..", Snackbar.LENGTH_SHORT).show();
                    alarm.setAlarm(context, sharedPref.getLong(COMMON.NOTIFICATION_FREQUENCY_KEY, 30));
                } else {
                    btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell_disabled));
                    Snackbar.make(view, "Notification Disabled..", Snackbar.LENGTH_SHORT).show();
                    alarm.cancelAlarm(context);
                }
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WaterActivity.this, StatsActivity.class));
            }
        });


        op50ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                selectedOption = 50;
                op50ml.setBackground(getDrawable(R.drawable.option_select_bg));
                op100ml.setBackground(getDrawable(outValue.resourceId));
                op150ml.setBackground(getDrawable(outValue.resourceId));
                op200ml.setBackground(getDrawable(outValue.resourceId));
                op250ml.setBackground(getDrawable(outValue.resourceId));
                opCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });


        op100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                selectedOption = 100;
                op50ml.setBackground(getDrawable(outValue.resourceId));
                op100ml.setBackground(getDrawable(R.drawable.option_select_bg));
                op150ml.setBackground(getDrawable(outValue.resourceId));
                op200ml.setBackground(getDrawable(outValue.resourceId));
                op250ml.setBackground(getDrawable(outValue.resourceId));
                opCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        op150ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                selectedOption = 150;
                op50ml.setBackground(getDrawable(outValue.resourceId));
                op100ml.setBackground(getDrawable(outValue.resourceId));
                op150ml.setBackground(getDrawable(R.drawable.option_select_bg));
                op200ml.setBackground(getDrawable(outValue.resourceId));
                op250ml.setBackground(getDrawable(outValue.resourceId));
                opCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        op200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                selectedOption = 200;
                op50ml.setBackground(getDrawable(outValue.resourceId));
                op100ml.setBackground(getDrawable(outValue.resourceId));
                op150ml.setBackground(getDrawable(outValue.resourceId));
                op200ml.setBackground(getDrawable(R.drawable.option_select_bg));
                op250ml.setBackground(getDrawable(outValue.resourceId));
                opCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        op250ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                selectedOption = 250;
                op50ml.setBackground(getDrawable(outValue.resourceId));
                op100ml.setBackground(getDrawable(outValue.resourceId));
                op150ml.setBackground(getDrawable(outValue.resourceId));
                op200ml.setBackground(getDrawable(outValue.resourceId));
                op250ml.setBackground(getDrawable(R.drawable.option_select_bg));
                opCustom.setBackground(getDrawable(outValue.resourceId));

            }
        });

        opCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }

                View picker_layout =   LayoutInflater.from(context).inflate(R.layout.custom_input_dialog, null);
                 MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
                alertDialogBuilder.setView(picker_layout);
                final TextInputLayout userInput = picker_layout.findViewById(R.id.etCustomInput);
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   @SuppressLint("SetTextI18n")
                   @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputText = userInput.getEditText().getText().toString();
                        if (!TextUtils.isEmpty(inputText)) {
                            tvCustom.setText(inputText + " ml");
                            selectedOption = Integer.parseInt(inputText);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
                op50ml.setBackground(getDrawable(outValue.resourceId));
                op100ml.setBackground(getDrawable(outValue.resourceId));
                op150ml.setBackground(getDrawable(outValue.resourceId));
                op200ml.setBackground(getDrawable(outValue.resourceId));
                op250ml.setBackground(getDrawable(outValue.resourceId));
                opCustom.setBackground(getDrawable(R.drawable.option_select_bg));
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Snackbar.make(findViewById(R.id.init_user_info_parent_layout), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 1000);
   }
}
