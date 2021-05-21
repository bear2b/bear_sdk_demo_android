package com.bear2b.sampleapp.ui.view.activities

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bear.common.sdk.BearCallback
import com.bear.common.sdk.BearSdk
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.fragments.*
import org.jetbrains.annotations.NotNull

class AdvancedSampleActivity : ArActivity() {

    @IdRes
    private val container = R.id.container

    override val scanTimeout = 10

    override val scanLineColor = Color.rgb(80, 44, 108)

    private val newCallback = object : BearCallback {
        override fun onArViewInitialized() {
            showAlert("ArView initialization completed")
            supportFragmentManager
                    .beginTransaction()
                    .add(container, MainFragment.newInstance())
                    .commit()
        }

        override fun onMarkerRecognized(markerId: Int, @NonNull assetsId: List<Int>) {
            showAlert("onMarkerRecognized marker id = $markerId assets id = $assetsId")
            replaceFragment(SuccessScanFragment.newInstance())
        }

        override fun onMarkerNotRecognized() = showAlert("onMarkerNotRecognized")

        override fun onAssetClicked(assetId: Int) = showAlert("onAssetClicked $assetId")

        override fun onError(@NonNull error: Throwable) = showAlert("Error occurred: $error")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bearHandler.registerBearCallback(newCallback)
        println(BearSdk.getInstance(this).deviceId)
    }

    override fun onDestroy() {
        bearHandler.unregisterBearCallback(newCallback)
        super.onDestroy()
    }

    override fun inflateContentView() {
        val view = layoutInflater.inflate(R.layout.activity_advanced_sample, null, false)
        addContentView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun openWebView(@NotNull url: String) = replaceFragment(WebViewFragment.newInstance(url))

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) bearHandler.cleanArView()
        super.onBackPressed()
    }

    fun showHistoryScreen() = replaceFragment(HistoryFragment.newInstance())

    fun showSamplePausingFragment() = replaceFragment(SamplePausingFragment.newInstance())

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showAlert(messageId: Int) {
        showAlert(getString(messageId))
    }
}