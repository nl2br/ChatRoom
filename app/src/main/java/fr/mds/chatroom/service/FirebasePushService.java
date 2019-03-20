package fr.mds.chatroom.service;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

import fr.mds.chatroom.activity.RoomActivity;
import fr.mds.chatroom.adapter.MessageViewAdapter;
import fr.mds.chatroom.model.Message;

public class FirebasePushService extends FirebaseMessagingService {

    private static final String TAG = "chatroom";

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "onNewToken " + s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String message = remoteMessage.getNotification().getBody();

        Log.d(TAG, "onMessageReceived " + message + " from " + user);

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

        Intent intent = new Intent("portal");
        intent.putExtra("user", user);
        intent.putExtra("message", message);
        broadcaster.sendBroadcast(intent);

    }

}
