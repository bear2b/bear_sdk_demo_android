package com.bear2b.sampleapp.ui.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bear.common.sdk.BearCallback;
import com.bear.common.sdk.ui.activities.main.ArActivity;
import com.bear2b.sampleapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicSampleActivity extends ArActivity {

    Button startScan;
    Button cleanView;
    boolean markerFound = false;

    private BearCallback newCallback = new BearCallback() {
        @Override
        public void onArViewInitialized() {
        }

        @Override
        public void onMarkerRecognized(final int i, @NonNull final List<Integer> list) {
            startScan.setVisibility(View.GONE);
            cleanView.setVisibility(View.VISIBLE);
            markerFound = true;
        }

        @Override
        public void onMarkerNotRecognized() {
        }

        @Override
        public void onAssetClicked(final int i) {
        }

        @Override
        public void onError(@NonNull final Throwable t) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBearHandler().registerBearCallback(newCallback);
    }

    @Override
    protected void onDestroy() {
        getBearHandler().unregisterBearCallback(newCallback);
        super.onDestroy();
    }

    @Override
    public void inflateContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_basic_sample, null, false);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        startScan = findViewById(R.id.btnStartScan);
        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBearHandler().startScan();
            }
        });

        cleanView = findViewById(R.id.cleanView);
        cleanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBearHandler().cleanView();
                markerFound = false;
                cleanView.setVisibility(View.GONE);
                startScan.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void openWebView(@NotNull String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getBearHandler().isScanRunning()) {
            getBearHandler().stopScan();
        } else if (markerFound) {
            getBearHandler().cleanView();
            markerFound = false;
            cleanView.setVisibility(View.GONE);
            startScan.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
