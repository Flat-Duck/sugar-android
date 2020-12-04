package ly.bithive.sugar.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import ly.bithive.sugar.AppController;
import ly.bithive.sugar.Chat;
import ly.bithive.sugar.R;
import ly.bithive.sugar.SessionManager;

import static ly.bithive.sugar.R.string.please_enter_message;
import static ly.bithive.sugar.util.COMMON.CHATS_URL;
import static ly.bithive.sugar.util.COMMON.UPDATE_URL;
import static ly.bithive.sugar.util.COMMON.USER_URL;

public class ProfileActivity extends AppCompatActivity {
TextView fullName,phoneNum,sugarType,yearInjury;
    SessionManager session;
    TextInputLayout name,phone, age,type,year,weight,height,kg,cm,pass;
ImageView EditBtn;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionManager(getApplicationContext());
        EditBtn = findViewById(R.id.editBTN);
        token = session.getToken();
        fullName = findViewById(R.id.fullname_field);
        phoneNum = findViewById(R.id.username_field);
        sugarType = findViewById(R.id.payment_label);
        yearInjury = findViewById(R.id.payment_desc);

      //  name = findViewById(R.id.full_name_profile1);
        age = findViewById(R.id.full_name_profile2);
    //    type = findViewById(R.id.full_name_profile3);
      //  year = findViewById(R.id.full_name_profile4);
        weight = findViewById(R.id.full_name_profile5);
        height = findViewById(R.id.full_name_profile6);
   //     phone = findViewById(R.id.full_name_profile7);


       // EditBtn = findViewById(R.id.editBTN);
        getProfile();
        final String token = session.getToken();
        Toast.makeText(this,token, Toast.LENGTH_LONG).show();
        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialogue();
            }
        });
    }

    private void showUpdateDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialogue_profile_update, null);

        builder.setTitle("تعديل البيانات");

        kg = view.findViewById(R.id.kg);
        cm = view.findViewById(R.id.cm);
        pass = view.findViewById(R.id.pass);

        kg.getEditText().setText(weight.getEditText().getText().toString());
        cm.getEditText().setText(height.getEditText().getText().toString());
        pass.getEditText().setText("");


        builder.setView(view);

        builder.setNeutralButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                updateData(kg.getEditText().getText().toString(),cm.getEditText().getText().toString(),pass.getEditText().getText().toString());
                getProfile();
            }
        });
        builder.setNegativeButton(R.string.cancel,null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateData(String kg,String cm,String pass) {
        JSONObject params = new JSONObject();
        try {
            params.put("weight", kg);
            params.put("height", cm);
            params.put("pass", pass);
            params.put("Content-Type", "application/json");
            params.put("Accept", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Accept", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "update_profile");

    }


    private void getProfile() {
        final String token = session.getToken();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dd = response.getJSONObject("data");
                    JSONObject user = dd.getJSONObject("user");
                    parseUser(user);
                    Log.d("XXx", user.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR", error.toString());
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;

            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "profile");
    }

    private void parseUser(JSONObject user) {
//الاسم. العمر. رقم الهاتف..نوع السكر.. الوزن.. تاريخ الاصابة
       try {
           fullName.setText(user.getString("name"));
           phoneNum.setText(user.getString("phone"));
           yearInjury.setText(user.getString("injury_year"));
           sugarType.setText("Type " + user.getString("diabetes_type"));
        //   name.getEditText().setText(user.getString("name"));
           age.getEditText().setText(user.getString("birth_date"));
        //   phone.getEditText().setText(user.getString(""));
        //   type.getEditText().setText(user.getString("diabetes_type"));
        //   year.getEditText().setText(user.getString(""));
           weight.getEditText().setText(user.getString("weight"));
           height.getEditText().setText(user.getString("height"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AAA",user.toString());
    }
}
