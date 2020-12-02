package ly.bithive.sugar.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ly.bithive.sugar.AppController;
import ly.bithive.sugar.R;
import ly.bithive.sugar.SessionManager;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.SqliteHelper;

public class StartActivity extends AppCompatActivity {
    SessionManager session;
    private static final String TAG = StartActivity.class.getSimpleName();

    private Button btnLogin;
    private Button btnLinkToRegister;
    TextInputEditText inputPhone;
    TextInputEditText inputPassword;
    //    private EditText inputPhone;
//    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SqliteHelper db;
    Button btnSignIn, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setContentView(R.layout.activity_start);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

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

        checkLogin();

    }

    private void checkLogin() {

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showSignInDialog();
        }

    }

    private void showSignInDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View picker_layout = LayoutInflater.from(this).inflate(R.layout.layout_login, null);
        builder.setView(picker_layout);
        builder.setTitle(R.string.sign_in_title);
        builder.setMessage(R.string.please_use_phone);
        inputPhone = picker_layout.findViewById(R.id.phone);
        inputPassword = picker_layout.findViewById(R.id.passwrod);


        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String phone = inputPhone.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                Log.d(TAG, "P:" + phone + "//W:" + password);
                login(phone, password);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void login(String phone, String password) {


        // Check for empty data in the form
        if (!phone.isEmpty() && !password.isEmpty()) {
            // login user
            sendLogin(phone, password);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(), R.string.please_enter_credentials, Toast.LENGTH_LONG).show();
        }


//
//        startActivity(new Intent(StartActivity.this,MainActivity.class));
//        finish();

    }


    /**
     * function to verify login details in mysql db
     */
    private void sendLogin(final String phone, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage(getString(R.string.logging_in));
        showDialog();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, COMMON.LOGIN_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        session.setLogin(true);
                        JSONObject data = jObj.getJSONObject("data");
                        String token = data.getString("accessToken");
                        session.setToken(token);
                    //    Toast.makeText(getApplicationContext(), "Token: " + token, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StartActivity.this, InitUserInfoActivity.class));
                        finish();
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                         hideDialog();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);
    }

//
//        };

    // Adding request to request queue
    //
    // }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}