package ly.bithive.sugar.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import ly.bithive.sugar.R;

public class StartActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnSignIn, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        showSignInDialog();
           btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInDialog();
            }
        });


    }

    private void showSignInDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View picker_layout = LayoutInflater.from(this).inflate(R.layout.layout_login, null);
        builder.setView(picker_layout);
        builder.setTitle("REGISTER");
        builder.setMessage("Please use email to register");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
startActivity(new Intent(StartActivity.this,MainActivity.class));
finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}