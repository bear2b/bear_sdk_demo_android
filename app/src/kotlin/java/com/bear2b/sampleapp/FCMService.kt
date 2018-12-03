package com.bear2b.sampleapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("DEV", "FCMService onMessageReceived: $remoteMessage")
        //TODO: handle message
    }
}