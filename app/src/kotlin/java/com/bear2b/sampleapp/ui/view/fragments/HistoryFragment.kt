package com.bear2b.sampleapp.ui.view.fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bear2b.sampleapp.R
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_history, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sample_history_item.setOnClickListener { (activity as AdvancedSampleActivity).showArSceneWithoutTracking(226620) }
    }

    override fun onResume() {
        super.onResume()
        (activity as AdvancedSampleActivity).bearHandler.cleanView()
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

}