package com.bear2b.sampleapp.ui.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup.*
import com.bear.common.sdk.BearCallback
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear2b.sampleapp.R
import kotlinx.android.synthetic.main.activity_basic_sample.*
import org.jetbrains.annotations.NotNull

class BasicSampleActivity : ArActivity() {

    var markerFound = false

    private var newCallback = object : BearCallback {

        override fun onArViewInitialized() {}

        override fun onMarkerRecognized(markerId: Int, @NonNull assetsId: List<Int>) {
            btnStartScan.visibility = View.GONE
            cleanView.visibility = View.VISIBLE
            markerFound = true
        }

        override fun onMarkerNotRecognized() {}

        override fun onAssetClicked(assetId: Int)  {}

        override fun onError(@NonNull error: Throwable)  {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bearHandler.registerBearCallback(newCallback)
    }

    override fun onDestroy() {
        bearHandler.unregisterBearCallback(newCallback)
        super.onDestroy()
    }

    override fun inflateContentView() {
        val view = layoutInflater.inflate(R.layout.activity_basic_sample, null, false)
        addContentView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        btnStartScan.setOnClickListener { bearHandler.startScan() }

        cleanView.setOnClickListener {
            bearHandler.cleanView()
            markerFound = false
            cleanView.visibility = View.GONE
            btnStartScan.visibility = View.VISIBLE
        }
    }

    override fun openWebView(@NotNull url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onBackPressed() = when {
        bearHandler.isScanRunning() -> bearHandler.stopScan()
        markerFound -> {
            bearHandler.cleanView()
            markerFound = false
            cleanView.visibility = View.GONE
            btnStartScan.visibility = View.VISIBLE
        }
        else -> super.onBackPressed()
    }

}