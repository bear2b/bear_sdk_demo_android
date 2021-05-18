package com.bear2b.sampleapp.ui.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.annotation.NonNull
import com.bear.common.sdk.BearCallback
import com.bear.common.sdk.listeners.scan.ArState
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.utils.extension.setCutoutMargin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_basic_sample.*
import org.jetbrains.annotations.NotNull

class BasicSampleActivity : ArActivity() {

    private val subscription = CompositeDisposable()

    private val newCallback = object : BearCallback {
        override fun onArViewInitialized() = showAlert("ArView initialization completed")

        override fun onMarkerRecognized(markerId: Int, @NonNull assetsId: List<Int>) =
                showAlert("onMarkerRecognized marker id = $markerId assets id = $assetsId")

        override fun onMarkerNotRecognized() = showAlert("onMarkerNotRecognized")

        override fun onAssetClicked(assetId: Int) = showAlert("onAssetClicked $assetId")

        override fun onError(@NonNull error: Throwable) = showAlert("Error occurred: $error")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bearHandler.registerBearCallback(newCallback)
    }

    override fun onDestroy() {
        bearHandler.unregisterBearCallback(newCallback)
        subscription.dispose()
        super.onDestroy()
    }

    override fun inflateContentView() {
        val view = layoutInflater.inflate(R.layout.activity_basic_sample, null, false)
        addContentView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        subscription.add(bearHandler.arStateObservingSubject        //Example how to use arStateObservingSubject for UI changes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.newState) {
                        ArState.SCANNING -> btnStartScan.text = "Stop Scan"
                        ArState.PROCESSING -> {
                            progressBar.visibility = View.VISIBLE
                            btnStartScan.visibility = View.GONE
                        }
                        ArState.TRACKING -> {
                            progressBar.visibility = View.GONE
                            cleanView.visibility = View.VISIBLE
                        }
                        else -> {
                            btnStartScan.text = "Start Scan"
                            cleanView.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            btnStartScan.visibility = View.VISIBLE
                        }
                    }
                })

        btnStartScan.setOnClickListener {
            when (bearHandler.getCurrentArState()) {
                ArState.IDLE -> bearHandler.startScan()
                ArState.SCANNING -> bearHandler.stopScan()
                else -> Unit
            }
        }

        cleanView.setOnClickListener { bearHandler.cleanArView() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window?.decorView?.rootWindowInsets?.displayCutout?.run {
                cleanView.setCutoutMargin(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            }
        }
    }

    override fun openWebView(@NotNull url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onBackPressed() = when (bearHandler.getCurrentArState()) {
        ArState.SCANNING -> bearHandler.stopScan()
        ArState.TRACKING -> bearHandler.cleanArView()
        else -> super.onBackPressed()
    }

    override fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showAlert(messageId: Int) {
        showAlert(getString(messageId))
    }
}