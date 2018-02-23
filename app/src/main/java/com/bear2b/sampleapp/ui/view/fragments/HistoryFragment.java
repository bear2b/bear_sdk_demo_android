package com.bear2b.sampleapp.ui.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bear2b.sampleapp.R;
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sample_history_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AdvancedSampleActivity) getActivity()).showArSceneWithoutTracking(226620);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdvancedSampleActivity) getActivity()).getHandler().cleanView();
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }
}