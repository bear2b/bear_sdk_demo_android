package com.bear2b.sampleapp.ui.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bear.common.sdk.BearCallback;
import com.bear.common.sdk.IBearHandler;
import com.bear.common.sdk.listeners.scan.ArState;
import com.bear.common.sdk.listeners.scan.ArStateChangeRegistrar;
import com.bear.common.sdk.ui.activities.main.ArActivity;
import com.bear2b.sampleapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bear2b.sampleapp.utils.extension.ExtensionsKt.setCutoutMargin;

public class BasicSampleActivity extends ArActivity {

    CompositeDisposable subscription = new CompositeDisposable();
    Button btnStartScan;
    Button cleanView;
    ProgressBar progressBar;
    public IBearHandler bearHandler = getBearHandler();

    @Override
    public int getScanTimeout() {
        return 10;
    }

    @Override
    public int getScanLineColor() {
        return Color.rgb(80, 44, 108);
    }

    private BearCallback newCallback = new BearCallback() {
        @Override
        public void onMarkerWithUnsupportedAsset() {
            showAlert("onMarkerWithUnsupportedAsset");
        }

        @Override
        public void onArViewInitialized() {
            showAlert("ArView initialization completed");
        }

        @Override
        public void onMarkerRecognized(final int i, @NonNull final List<Integer> list) {
            showAlert("onMarkerRecognized, marker id = " + i + " assets id = " + list.toString());
        }

        @Override
        public void onMarkerNotRecognized() {
            showAlert("onMarkerNotRecognized");
        }

        @Override
        public void onAssetClicked(final int i) {
            showAlert("onAssetClicked " + i);
        }

        @Override
        public void onError(@NonNull final Throwable t) {
            showAlert("Error occurred: " + t.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bearHandler.registerBearCallback(newCallback);
    }

    @Override
    protected void onDestroy() {
        bearHandler.unregisterBearCallback(newCallback);
        subscription.dispose();
        super.onDestroy();
    }

    @Override
    public void inflateContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_basic_sample, null, false);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        progressBar = findViewById(R.id.progressBar);
        btnStartScan = findViewById(R.id.btnStartScan);
        cleanView = findViewById(R.id.cleanView);

        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bearHandler.getCurrentArState() == ArState.IDLE) bearHandler.startScan();
                else if (bearHandler.getCurrentArState() == ArState.SCANNING) bearHandler.stopScan();
            }
        });

        cleanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bearHandler.cleanArView();
            }
        });

        subscription.add(bearHandler.getArStateObservingSubject()        //Example how to use arStateObservingSubject for UI changes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArStateChangeRegistrar>() {
                    @Override
                    public void accept(ArStateChangeRegistrar arStateChangeRegistrar) {
                        switch (arStateChangeRegistrar.getNewState()) {
                            case IDLE:
                                btnStartScan.setText("Start Scan");
                                cleanView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                btnStartScan.setVisibility(View.VISIBLE);
                                break;
                            case SCANNING:
                                btnStartScan.setText("Stop Scan");
                                break;
                            case PROCESSING:
                                progressBar.setVisibility(View.VISIBLE);
                                btnStartScan.setVisibility(View.GONE);
                                break;
                            case TRACKING:
                                progressBar.setVisibility(View.GONE);
                                cleanView.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            if (displayCutout != null)
                setCutoutMargin(view.findViewById(R.id.cleanView), displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
        }
    }

    @Override
    public void openWebView(@NotNull String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        switch (bearHandler.getCurrentArState()) {
            case SCANNING:
                bearHandler.stopScan();
                break;
            case TRACKING:
                bearHandler.cleanArView();
                break;
            default:
                super.onBackPressed();
        }
    }

    @Override
    public void showAlert(@NotNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAlert(int messageId) {
        showAlert(getString(messageId));
    }
}
