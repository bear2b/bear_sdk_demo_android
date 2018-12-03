package com.bear2b.sampleapp

import com.bear.common.sdk.BearSdk
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class FcmInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        FirebaseInstanceId.getInstance().token?.let { BearSdk.getInstance(this).registerFirebaseToken(it) }
    }
}