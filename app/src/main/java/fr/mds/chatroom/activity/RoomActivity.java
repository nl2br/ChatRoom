package fr.mds.chatroom.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;

import fr.mds.chatroom.R;
import fr.mds.chatroom.adapter.MessageViewAdapter;
import fr.mds.chatroom.model.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RoomActivity extends Activity {

    private static final String TAG = "chatroom";

    private ListView lv_chat_room_message_list;
    private EditText et_chat_room_message;
    private Button bt_chat_room_submit;

    private SharedPreferences sharedPreferences;

    private ArrayList<Message> messages = new ArrayList<Message>();;

    private MessageViewAdapter messageAdapter;

    private String currentUserLogin;

    private BroadcastReceiver receiver;

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        bt_chat_room_submit = findViewById(R.id.bt_chat_room_submit);
        et_chat_room_message = findViewById(R.id.et_chat_room_message);
        lv_chat_room_message_list = findViewById(R.id.lv_chat_room_message_list);

        // retrieve data from the sharedPreferences
        SharedPreferences prefs = getSharedPreferences("fr.mds.chatroom", Context.MODE_PRIVATE);
        currentUserLogin = prefs.getString("login","");
        Log.d(TAG,"user : " + currentUserLogin);

        // ADAPTER
        messageAdapter = new MessageViewAdapter(this, messages);
        lv_chat_room_message_list.setAdapter(messageAdapter);

        bt_chat_room_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendMessage(et_chat_room_message.getText().toString());
            }
        });

        // BROADCAST RECEIVER
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String user = intent.getStringExtra("user");
                    String message = intent.getStringExtra("message");
                    receiveMessage(message, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

    }

    void sendMessage(String content){
        Log.d(TAG,"My message with the login : " + currentUserLogin + " say : " + content);

        // add the content
        addMessage(content, currentUserLogin);
        messageAdapter.notifyDataSetChanged();

        //post
        sendPost(content);
    }

    private void sendPost(String content){

        String messageToJson = "{\"to\":\"/topics/chat\",\"data\":{\"user\":\"" + currentUserLogin + "\"},\"notification\":{\"body\":\"" + content +"\"}}";

        RequestBody body = RequestBody.create(JSON, messageToJson);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Authorization","key=AAAAwDk-sl0:APA91bFgaYBIm1E9cK7snWPiIGiiLxgbWYnqA-PDOOHJBRZg5Pog0QDmzer3-9YTN9OzLh1K7xHkI7QzNYKEKzpeb1a_gCvrLXqVAapP1QTci8yIb4u_jUdap-6bmXbwGdmgb7sXvn6U")
                .addHeader("Content-type","application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "erreur de merde" + call.request().body().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "response " + response.body().string());
            }
        });

    }

    void receiveMessage(String content, String user){
        Log.d(TAG,"From outside : user " + user + " say : " + content);

        // add the content
        addMessage(content, user);
        messageAdapter.notifyDataSetChanged();
    }

    void addMessage(String content, String userLogin) {
        Message message = new Message(content,userLogin);
        messages.add(message);
    }

@Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(RoomActivity.this).registerReceiver((receiver),
                new IntentFilter("portal")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(RoomActivity.this).unregisterReceiver(receiver);

    }

}
