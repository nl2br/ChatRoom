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

import java.util.ArrayList;
import java.util.Currency;

import fr.mds.chatroom.R;
import fr.mds.chatroom.adapter.MessageViewAdapter;
import fr.mds.chatroom.model.Message;

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
        // initialise adapter with messages
        lv_chat_room_message_list.setAdapter(messageAdapter);

        bt_chat_room_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TEMP add messages
            sendMessage(et_chat_room_message.getText().toString());

            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String user = intent.getStringExtra("user");
                    String message = intent.getStringExtra("message");
                    sendMessage(message, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

    }

    void sendMessage(String content){
        Log.d(TAG,"user : " + currentUserLogin + " message: " + content);

        // add the content
        addMessage(content, currentUserLogin);
        messageAdapter.notifyDataSetChanged();
        //post
    }

    void sendMessage(String content, String user){
        Log.d(TAG,"From outside : user " + user + " say : " + content);

        // add the content
        addMessage(content, user);
        messageAdapter.notifyDataSetChanged();
        //post
    }

    void addMessage(String content, String userLogin) {
        Message message = new Message(content,userLogin);
        messages.add(message);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(RoomActivity.this).registerReceiver((receiver),
                new IntentFilter("test")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(RoomActivity.this).unregisterReceiver(receiver);

    }

}
