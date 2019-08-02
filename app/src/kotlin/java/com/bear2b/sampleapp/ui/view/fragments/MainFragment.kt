package com.bear2b.sampleapp.ui.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bear.common.sdk.IBearHandler
import com.bear.common.sdk.listeners.flash.FlashStatus
import com.bear.common.sdk.listeners.flash.FlashStatusListener
import com.bear.common.sdk.ui.activities.main.ArActivity
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity
import com.bear2b.sampleapp.ui.view.activities.ExampleActivity
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.annotations.Nullable

class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var bearHandler : IBearHandler

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
        btnStartScan.setOnClickListener { bearHandler.startScan() }
        btnFlash.setOnClickListener {
            when (bearHandler.isFlashEnabled()) {
                true -> bearHandler.disableFlash()
                false -> bearHandler.enableFlash()
            }
        }

        btnMarker.setOnClickListener {
            if (bearHandler.isScanRunning()) bearHandler.stopScan()
            (activity as ArActivity).showArSceneWithoutTracking(226620) //some marker id for example
        }

        toggle = ActionBarDrawerToggle(activity, dlMain, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        dlMain.addDrawerListener(toggle)
        toggle.syncState()
        nvMain.setNavigationItemSelectedListener(this)
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