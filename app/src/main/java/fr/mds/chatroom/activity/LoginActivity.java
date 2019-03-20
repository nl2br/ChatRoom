package fr.mds.chatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import fr.mds.chatroom.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "chatroom";

    private EditText et_chat_login_login;
    private Button bt_chat_login_submit;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login);

        bt_chat_login_submit = findViewById(R.id.bt_chat_login_submit);
        et_chat_login_login = findViewById(R.id.et_chat_login_login);

        bt_chat_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // shared preferences, accessible only on this app
                sharedPreferences = getSharedPreferences("fr.mds.chatroom", Context.MODE_PRIVATE);
                // create the editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // add data to the editor
                editor.putString("login",et_chat_login_login.getText().toString());
                // close the editor
                editor.apply();

                // go to the chatroom view
                // create the EXPLICIT intent and specify the destination
                Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("chat")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe success";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failure !!!";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
