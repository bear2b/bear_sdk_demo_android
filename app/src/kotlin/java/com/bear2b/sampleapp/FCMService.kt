package com.bear2b.sampleapp

import android.util.Log
import com.bear.common.sdk.BearSdk
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        token?.let { BearSdk.getInstance(this).registerFirebaseToken(it) }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("DEV", "FCMService onMessageReceived: $remoteMessage")
        //TODO: handle message
    }
}