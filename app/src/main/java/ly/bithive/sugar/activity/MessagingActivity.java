package ly.bithive.sugar.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ly.bithive.sugar.R.string.please_enter_message;
import static ly.bithive.sugar.util.COMMON.CHATS_URL;
public class MessagingActivity extends AppCompatActivity {

    private ListView rv;
    private View btnSend;
    private EditText editText;
    private List<Chat> chatMessages;
    private MessageAdapter adapter;
    SessionManager session;
     String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImI4NmY2OTljNTg3N2MxMjEyNTlmYWEwNDU0ZTRmOTA3M2RlN2E2YzBmYzk1YTMwNDVlMGFhODQyNjhlZWVkOTUzZjQ0MjAxN2U0ZjZlMTc0In0.eyJhdWQiOiIxIiwianRpIjoiYjg2ZjY5OWM1ODc3YzEyMTI1OWZhYTA0NTRlNGY5MDczZGU3YTZjMGZjOTVhMzA0NWUwYWE4NDI2OGVlZWQ5NTNmNDQyMDE3ZTRmNmUxNzQiLCJpYXQiOjE2MDU3ODAwNDksIm5iZiI6MTYwNTc4MDA0OSwiZXhwIjoxNjM3MzE2MDQ5LCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.O9BuMT24GK4xxGrW0zlrHBwmkcX8ddlNL17-GtpDW3m1WHmLl5GaY2nN4EAtDMAEXOTQAPkuqs8nLj4i-9rcrwugLiHjpTomUaS51-WLUrJJGXKIVw2VpN3yUKs5Rw4inWtHP-cpJwbQU4_iZdvm_3zHSTq5_OVOIVb6qybAo8A2SN7TijzGiNbnhvDSmqdVR4isZmVwcFdw75LGgNCCuuy2t4sziOyx4iwJEcOoK0s1gL1buNqeIKyvUdbnX3Mw72sRNCV7clrPQxWcaExJh5w8Sk2AZ8iENvn4yvRVSY4-_kke6wu6Ko2BB3cEiXpWxoA_X7dZHaGzAjEZIaQJwdC9aI963_DWNHWMynsGYN3UeH56QY0s14kKhawMfqBP5qGV2fOpRVc5Q701Xs82G0_qY1WFSII0K3RbWD6lHZasIoy_2m-iKZfhDruPZCiyD2c022cvMyxzc764ORbQF8En_O45PCRTXL8XMQVaejGGX4H_byUbOd81ke6wZZQBaLwodxUsDealQ9fDK6ddpI83j1jF5fm0P37_5GUYYpoT256SgH3iOyF9-LOOWiuN4DFcEvwf47Xdi43OFXaR-gQCqbdn7EVogefQl_jSAqQQwsYRwkqx29zENtZPeIjVqTo9uIahk9OUFlPJW2vahwiHhmXc_GdkCs0roPjjNjU";// session.getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        session = new SessionManager(getApplicationContext());
        token = session.getToken();
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
}