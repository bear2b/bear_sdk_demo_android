package com.bear2b.sampleapp.ui.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.bear.common.sdk.BearHandler
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity
import android.support.annotation.Nullable
import android.support.v4.app.ShareCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bear2b.sampleapp.R
import kotlinx.android.synthetic.main.success_scan_fragment.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class SuccessScanFragment : Fragment() {

    private lateinit var handler : BearHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        handler = (activity as AdvancedSampleActivity).handler
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.success_scan_fragment, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnScreenshot.setOnClickListener { SuccessScanFragmentPermissionsDispatcher.shareImageWithCheck(this@SuccessScanFragment) }
        btnSamplePausingFragment.setOnClickListener {
            (activity as AdvancedSampleActivity).pauseArView()
            (activity as AdvancedSampleActivity).showSamplePausingFragment()
        }
    }

    override fun onPause() {
        // Screenshot may not be completed when fragment is paused
        handler.cancelScreenshot()
        super.onPause()
    }

    companion object {
        fun newInstance() = SuccessScanFragment()
    }

    @NeedsPermission("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    fun shareImage() = handler.takeScreenshot(object : BearHandler.ScreenshotCallback {
        override fun onComplete(imageUri: Uri) {
            ShareCompat.IntentBuilder
                    .from(activity)
                    .setType("image/*")
                    .setStream(imageUri)
                    .startChooser()
        }
    })

    @OnShowRationale("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    fun showRationaleForCamera(request: PermissionRequest) {
        AlertDialog.Builder(activity)
                .setMessage("request permission")
                .setPositiveButton("allow") { _, _ -> request.proceed() }
                .setNegativeButton("deny") { _, _ -> request.cancel() }
                .show()
    }

    @OnPermissionDenied("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    fun showDeniedStorage() = Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()

    @OnNeverAskAgain("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    fun showNeverAskForStorage() = Toast.makeText(activity, "never ask again", Toast.LENGTH_SHORT).show()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        SuccessScanFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }
}