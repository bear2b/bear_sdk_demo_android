package com.bear2b.sampleapp.ui.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bear2b.sampleapp.R;
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity;

public class SamplePausingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_for_pausing, container, false);
    }

    @Override
    public void onDestroyView() {
        /* You need to call resumeArView() because pauseArView() was called when starting this fragment */
        ((AdvancedSampleActivity) getActivity()).resumeArView();
        super.onDestroyView();
    }

    public static SamplePausingFragment newInstance() {
        return new SamplePausingFragment();
    }
}
