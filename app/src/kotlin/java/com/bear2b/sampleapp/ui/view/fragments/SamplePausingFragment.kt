package com.bear2b.sampleapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity

class SamplePausingFragment : Fragment() {

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sample_for_pausing, container, false)

    override fun onDestroyView() {
        /* You need to call resumeArView() because pauseArView() was called when starting this fragment */
        (activity as AdvancedSampleActivity).resumeArView()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = SamplePausingFragment()
    }
}