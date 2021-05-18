package com.bear2b.sampleapp.ui.view.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.bear2b.sampleapp.R

class ExampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE.or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN).or(View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}