package ly.bithive.sugar.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ly.bithive.sugar.AppController;
import ly.bithive.sugar.R;
import ly.bithive.sugar.SessionManager;

import static ly.bithive.sugar.R.string.please_enter_message;
import static ly.bithive.sugar.util.COMMON.USER_URL;

public class ProfileActivity extends AppCompatActivity {

    SessionManager session;
ImageView EditBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionManager(getApplicationContext());
        EditBtn = findViewById(R.id.editBTN);
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
        builder.setView(R.layout.dot_layout);
        builder.setTitle(getString(R.string.sport_alert_title));

        builder.setNeutralButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (editText.getText().toString().trim().equals("")) {
//                    Toast.makeText(MessagingActivity.this, please_enter_message, Toast.LENGTH_SHORT).show();
//                } else {
//                    String message = editText.getText().toString();
                      updateData();
//                }

            }
        });
        builder.setNegativeButton(R.string.cancel,null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateData() {
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

    }
//
//    private void parseData(@NonNull JSONObject response) throws JSONException {
//        Toast.makeText(ProfileActivity.this, "// Start to parse", Toast.LENGTH_SHORT).show();
//        JSONObject item = response.getJSONObject("data");
//        clink.setId(item.getInt("id"))
//                .setName(item.getString("name"))
//                .setAddress(item.getString("address"))
//                .setLatitude(item.getString("latitude"))
//                .setLongitude(item.getString("longitude"))
//                .setPhone_number(item.getString("phone_number"))
//                .setStatus(Boolean.parseBoolean(item.getString("status")))
//                .setVisible(Boolean.parseBoolean(item.getString("visible")));
//
//        TextView tvName = findViewById(R.id.name);
//        TextView tvAddress = findViewById(R.id.designation);
//        tvName.setText(clink.getName());
//        tvAddress.setText(clink.getAddress());
//
//        Toast.makeText(ProfileActivity.this, "// Data Sent", Toast.LENGTH_SHORT).show();
//    }
}
