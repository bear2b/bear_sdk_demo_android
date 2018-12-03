package com.bear2b.sampleapp.ui.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bear2b.sampleapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basic_sample.setOnClickListener { startActivity(Intent(this, BasicSampleActivity::class.java)) }

        advanced_sample.setOnClickListener { startActivity(Intent(this, AdvancedSampleActivity::class.java)) }
    }
}