package com.bear2b.sampleapp.ui.view.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bear.common.sdk.BearSdk;
import com.bear.common.sdk.BearCallback;
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
        public void onArViewInitialized() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(AdvancedSampleActivity.this)
                            .setMessage("ArView initialization completed")
                            .show();
                }
            });
            fragmentManager
                    .beginTransaction()
                    .add(container, MainFragment.newInstance())
                    .commit();
        }

        @Override
        public void onMarkerRecognized(final int i, @NonNull final List<Integer> list) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(AdvancedSampleActivity.this)
                            .setTitle("onMarkerRecognized")
                            .setMessage("marker id = " + i + " assets id = " + list.toString())
                            .show();
                }
            });
            replaceFragment(SuccessScanFragment.newInstance());
        }

        @Override
        public void onMarkerNotRecognized() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(AdvancedSampleActivity.this)
                            .setMessage("onMarkerNotRecognized")
                            .show();
                }
            });
        }

        @Override
        public void onAssetClicked(final int i) {
            Toast.makeText(AdvancedSampleActivity.this, "onAssetClicked id = " + i, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(@NonNull final Throwable t) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(AdvancedSampleActivity.this)
                            .setTitle("Error occurred")
                            .setMessage(t.toString())
                            .show();
                }
            });
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
        if (fragmentManager.getBackStackEntryCount() == 1) bearHandler.cleanView();
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
}
