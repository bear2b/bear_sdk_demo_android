package com.bear2b.sampleapp.ui.view.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bear.common.sdk.BearCallback;
import com.bear.common.sdk.BearSdk;
import com.bear.common.sdk.IBearHandler;
import com.bear.common.sdk.ui.activities.main.ArActivity;
import com.bear2b.sampleapp.R;
import com.bear2b.sampleapp.ui.view.fragments.HistoryFragment;
import com.bear2b.sampleapp.ui.view.fragments.MainFragment;
import com.bear2b.sampleapp.ui.view.fragments.SamplePausingFragment;
import com.bear2b.sampleapp.ui.view.fragments.SuccessScanFragment;
import com.bear2b.sampleapp.ui.view.fragments.WebViewFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class AdvancedSampleActivity extends ArActivity {

    private FragmentManager fragmentManager = getFragmentManager();
    public IBearHandler bearHandler = getBearHandler();

    @IdRes
    private int container = R.id.container;

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
            fragmentManager
                    .beginTransaction()
                    .add(container, MainFragment.newInstance())
                    .commit();
        }

        @Override
        public void onMarkerRecognized(final int i, @NonNull final List<Integer> list) {
            showAlert("onMarkerRecognized, marker id = " + i + " assets id = " + list.toString());
            replaceFragment(SuccessScanFragment.newInstance());
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
        System.out.println(BearSdk.getInstance(this).getDeviceId());
    }

    @Override
    protected void onDestroy() {
        bearHandler.unregisterBearCallback(newCallback);
        super.onDestroy();
    }

    @Override
    public void inflateContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_advanced_sample, null, false);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void openWebView(@NotNull String url) {
        replaceFragment(WebViewFragment.newInstance(url));
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) bearHandler.cleanArView();
        super.onBackPressed();
    }

    public void showHistoryScreen() {
        replaceFragment(HistoryFragment.newInstance());
    }

    public void showSamplePausingFragment() {
        replaceFragment(SamplePausingFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commit();
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
