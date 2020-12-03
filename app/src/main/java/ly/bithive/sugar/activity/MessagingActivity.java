package ly.bithive.sugar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import ly.bithive.sugar.AppController;
import ly.bithive.sugar.Chat;
import ly.bithive.sugar.MessageAdapter;
import ly.bithive.sugar.R;
import ly.bithive.sugar.SessionManager;
import ly.bithive.sugar.util.COMMON;
import ly.bithive.sugar.util.SqliteHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static ly.bithive.sugar.R.string.please_enter_message;
import static ly.bithive.sugar.util.COMMON.CHATS_URL;
import static ly.bithive.sugar.util.COMMON.KEY_TIME;
import static ly.bithive.sugar.util.COMMON.SYNC_URL;
import static ly.bithive.sugar.util.SqliteHelper.KEY_DATE;
import static ly.bithive.sugar.util.SqliteHelper.KEY_GLYCEMIA;
import static ly.bithive.sugar.util.SqliteHelper.KEY_PERIOD;

public class MessagingActivity extends AppCompatActivity {

    private ListView rv;
    private View btnSend;
    private EditText editText;
    private List<Chat> chatMessages;
    private MessageAdapter adapter;
    SessionManager session;
    SqliteHelper sqliteHelper;
     String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMDY0MGYzNzkzZDljZjkwNWNmOGM0MzNiZDZiMjU0YWE0YTk2MDg0NGZiMjEwYjg3ZGM1NTIwOTNjYTc0NTkzMGY4MDk0ZWIzMTliYzNiMGEiLCJpYXQiOjE2MDcwMTg1NDIsIm5iZiI6MTYwNzAxODU0MiwiZXhwIjoxNjM4NTU0NTQyLCJzdWIiOiI2Iiwic2NvcGVzIjpbXX0.Uj_LTUX8HoQaq313T8uwjMEZvUMDAZ366ATMwFO0m8LT_8sqkpj8TXLMNY2owonZcjAHP78e8Vv_HyUn9r6m9tFBCZWX-XNQlB65QJn7Oy941AqO408FyWKoDO1vrXy8cNPjmBm4eeBFKZvVQhSTvbF0UMfl4Ijs0R14OxLhGDIJhFGgOOg0V8z1m6tDvQcz3LF2rThovqjgz_Igl3ORpnrvKGzaYetOWtXRZIvhH33gXcBlDCXMjYxS55XK1uW0XrbhrZqKjHAU9V9oqIhB3imzwp-BjfYVHyHMRWkZrHdT6Hl5Ep7E4hFCezRPZdIZTla0HGk65EpnURfduPw56wss7CZiuDe195a1NrJxnJOgxlIwMt1OdGgbZOcfSqR0OzeJqc3OrC0c4Mfv5ImWjNZ2w7lIIehfPJ_3sYoX1nyqDkkMNSdhEbRwx2W-7KR9S3hT5DeWUDSY2NmoU90ZfLnrgL-nRqJIIqYWJa9VsKml-rHdCZg-rVNvWv55MqkROoH7KEZ0jyElf3i3-3qogppZKX2Hj-wFGn0pmuW8PG9ZxamwWvhByLsW_mOsX6E5MbQzE2WFPxBclMYlCy8HlhDxE5C6rN18jEr5MsyhxvaoNkHnoDOMlO8QoFvluqXgKOeHX0SFkPFbDA4xTWCqDCD8yMg1lhbjSnjwbAZubvA";// session.getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        session = new SessionManager(getApplicationContext());
        token = session.getToken();
         sqliteHelper = new SqliteHelper(this);
        chatMessages = new ArrayList<>();
        rv = findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        editText = (EditText) findViewById(R.id.msg_type);
        Toast.makeText(this,token,Toast.LENGTH_LONG).show();
        //set ListView adapter first
        adapter = new MessageAdapter(this, R.layout.item_chat_in, chatMessages);
        rv.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(MessagingActivity.this, please_enter_message, Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage();
                }
            }
        });
        getChats();
        syncData();
    }
    private void getChats() {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CHATS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray chats = response.getJSONObject("data").getJSONArray("chats");
                    parseChats(chats);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;

            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "chats");
    }

    private void parseChats(JSONArray chats) {
        for (int i = 0; i < chats.length(); i++) {
            try {
                JSONObject chat = chats.getJSONObject(i);
                Chat chatMessage = new Chat(chat.getString("message"), chat.getBoolean("ismine"));
                chatMessages.add(chatMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        adapter.notifyDataSetChanged();
    }
    private void sendMessage() {
        if (editText.getText().toString().trim().equals("")) {
            Toast.makeText(MessagingActivity.this, please_enter_message, Toast.LENGTH_SHORT).show();
        } else {
            String message = editText.getText().toString();
            syncMessage(message);
        }
    }

    private void syncMessage(final String message) {
        JSONObject params = new JSONObject();
        try {
            params.put("message", message);
            params.put("Content-Type", "application/json");
            params.put("Accept", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CHATS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Chat chatMessage = new Chat(editText.getText().toString(), true);
                chatMessages.add(chatMessage);
                adapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "chat");
    }

    void syncData(){
        JSONArray  array = prepareAnalysisData();

        syncANA(array);
    }

    private JSONArray prepareAnalysisData() {
        Log.d("DB","Start");
        JSONArray reportItems = new JSONArray();

            String period,value, date,time;

            try (Cursor cursor = sqliteHelper.getAllAnalysis()) {
                while (cursor.moveToNext()) {
                    if (cursor != null) {
                        int dateIndex = cursor.getColumnIndexOrThrow(KEY_DATE);
                        int timeIndex = cursor.getColumnIndexOrThrow(KEY_TIME);
                        int periodIndex = cursor.getColumnIndexOrThrow(KEY_PERIOD);
                        int valueIndex = cursor.getColumnIndexOrThrow(KEY_GLYCEMIA);
                         String  dat = cursor.getString(dateIndex);
//                         date = COMMON.convertDate(dat);
                        period = cursor.getString(periodIndex);
                        value = cursor.getString(valueIndex);
                        time = cursor.getString(timeIndex);
                        JSONObject analysis = new JSONObject();
                        try {
                            analysis.put(KEY_DATE+"_"+KEY_TIME, dat+ " "+time);
                            analysis.put(KEY_PERIOD, period);
                            analysis.put(KEY_GLYCEMIA, value);
                            reportItems.put(analysis);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        return reportItems;

        }}

    private void syncANA(final JSONArray array) {
        JSONObject params = new JSONObject();
        try {
            params.put("analysis", array);
            params.put("Content-Type", "application/json");
            params.put("Accept", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("XXXXXX",params.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SYNC_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")){


                    }else {
                        Log.d("AAAAAA",response.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("XXXXXX",error.toString());
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "analysis");
    }


}