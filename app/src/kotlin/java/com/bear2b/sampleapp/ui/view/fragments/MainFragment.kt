package com.bear2b.sampleapp.ui.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bear.common.sdk.IBearHandler
import com.bear.common.sdk.listeners.flash.FlashStatus
import com.bear.common.sdk.listeners.flash.FlashStatusListener
import com.bear.common.sdk.listeners.scan.ArState
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity
import com.bear2b.sampleapp.ui.view.activities.ExampleActivity
import com.bear2b.sampleapp.utils.extension.setCutoutMargin
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.annotations.Nullable

class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var bearHandler: IBearHandler

    private val subscription = CompositeDisposable()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        bearHandler = (activity as AdvancedSampleActivity).bearHandler
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.main_fragment, container, false)

    private val flashListener: FlashStatusListener = object : FlashStatusListener {
        override fun onStatusChanged(status: FlashStatus) =
                Toast.makeText(activity, "FlashStatus = ${status.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscription.add(bearHandler.arStateObservingSubject        //Example how to use arStateObservingSubject for UI changes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.newState) {
                        ArState.SCANNING -> btnStartScan?.text = "Stop Scan"
                        else -> btnStartScan?.text = "Start scan"
                    }
                })

        btnStartScan.setOnClickListener {
            when (bearHandler.getCurrentArState()) {
                ArState.IDLE -> bearHandler.startScan()
                ArState.SCANNING -> bearHandler.stopScan()
                else -> Unit
            }
        }
        btnFlash.setOnClickListener {
            when (bearHandler.isFlashEnabled()) {
                true -> bearHandler.disableFlash()
                false -> bearHandler.enableFlash()
            }
        }

        btnMarker.setOnClickListener {
            if (bearHandler.getCurrentArState() == ArState.SCANNING) bearHandler.stopScan()
            (activity as ArActivity).showArSceneWithoutTracking(226620) //some marker id for example
        }

        toggle = ActionBarDrawerToggle(activity, dlMain, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        dlMain.addDrawerListener(toggle)
        toggle.syncState()
        nvMain.setNavigationItemSelectedListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            activity?.window?.decorView?.rootWindowInsets?.displayCutout?.run {
                cutContainer.setCutoutMargin(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            }
    }

    override fun onResume() {
        super.onResume()
        bearHandler.addFlashListener(flashListener)
    }

    override fun onPause() {
        super.onPause()
        bearHandler.removeFlashListener(flashListener)
    }

    private fun closeDrawer() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) dlMain.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_history -> (activity as AdvancedSampleActivity).showHistoryScreen()
            R.id.nav_example_activity ->  startActivity(Intent(activity, ExampleActivity::class.java))
        }
        closeDrawer()
        return false
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}