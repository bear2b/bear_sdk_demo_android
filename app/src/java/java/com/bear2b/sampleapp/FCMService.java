package com.bear2b.sampleapp;

import android.util.Log;

import com.bear.common.sdk.BearSdk;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (token != null) {
            BearSdk.getInstance(this).registerFirebaseToken(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("DEV", "FCMService onMessageReceived: " + remoteMessage.toString());
        //TODO: handle message
    }
}
