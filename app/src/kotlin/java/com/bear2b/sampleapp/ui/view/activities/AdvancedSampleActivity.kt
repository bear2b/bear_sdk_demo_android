package com.bear2b.sampleapp.ui.view.activities

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.view.ViewGroup.*
import android.widget.Toast
import com.bear.common.sdk.BearCallback
import com.bear.common.sdk.BearSdk
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear.common.sdk.ui.activities.main.InitStatusListener
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.fragments.*
import org.jetbrains.annotations.NotNull

class AdvancedSampleActivity : ArActivity() {

    @IdRes
    private var mContainer = R.id.container

    override val scanTimeout = 10

    override val scanLineColor = Color.rgb(80,44,108)

    private var newCallback = object : BearCallback {

        override fun onMarkerRecognized(markerId: Int, @NonNull assetsId: List<Int>) {
            Handler(Looper.getMainLooper()).post {
                AlertDialog.Builder(this@AdvancedSampleActivity)
                        .setTitle("onMarkerRecognized")
                        .setMessage("marker id = $markerId assets id = $assetsId")
                        .show()
            }
            replaceFragment(SuccessScanFragment.newInstance())
        }

        override fun onMarkerNotRecognized() {
            Handler(Looper.getMainLooper()).post {
                AlertDialog.Builder(this@AdvancedSampleActivity)
                        .setMessage("onMarkerNotRecognized")
                        .show()
            }
        }

        override fun onAssetClicked(assetId: Int) {
            Toast.makeText(this@AdvancedSampleActivity, "onAssetClicked id = $assetId", Toast.LENGTH_SHORT).show()
        }

        override fun onError(@NonNull error: Throwable, isMarkerFromScan: Boolean) {
            Handler(Looper.getMainLooper()).post {
                AlertDialog.Builder(this@AdvancedSampleActivity)
                        .setTitle("onError while recognizing marker")
                        .setMessage("isMarkerFromScan = $isMarkerFromScan error = $error")
                        .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler.registerBearCallback(newCallback)
        addArViewInitListener(object : InitStatusListener {
            override fun onInitComplete() {
                AlertDialog.Builder(this@AdvancedSampleActivity)
                        .setMessage("ArView initialization completed")
                        .show()
                supportFragmentManager
                        .beginTransaction()
                        .add(mContainer, MainFragment.newInstance())
                        .commit()
            }
        })
        System.out.println(BearSdk.getInstance(this).deviceId)
    }

    override fun onDestroy() {
        handler.unregisterBearCallback(newCallback)
        super.onDestroy()
    }

    override fun inflateContentView() {
        val view = layoutInflater.inflate(R.layout.activity_advanced_sample, null, false)
        addContentView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun openWebView(@NotNull url: String) = replaceFragment(WebViewFragment.newInstance(url))

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) handler.cleanView()
        super.onBackPressed()
    }

    fun showHistoryScreen() = replaceFragment(HistoryFragment.newInstance())

    fun showSamplePausingFragment() = replaceFragment(SamplePausingFragment.newInstance())

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(mContainer, fragment)
                .addToBackStack(null)
                .commit()
    }
}