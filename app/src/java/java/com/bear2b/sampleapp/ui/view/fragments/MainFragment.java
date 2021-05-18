package com.bear2b.sampleapp.ui.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bear.common.sdk.IBearHandler;
import com.bear.common.sdk.listeners.flash.FlashStatus;
import com.bear.common.sdk.listeners.flash.FlashStatusListener;
import com.bear.common.sdk.listeners.scan.ArState;
import com.bear.common.sdk.listeners.scan.ArStateChangeRegistrar;
import com.bear.common.sdk.ui.activities.main.ArActivity;
import com.bear2b.sampleapp.R;
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity;
import com.bear2b.sampleapp.ui.view.activities.ExampleActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bear2b.sampleapp.utils.extension.ExtensionsKt.setCutoutMargin;

public class MainFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout dlMain;
    Toolbar toolbar;
    NavigationView nvMain;
    ActionBarDrawerToggle toggle;
    Button btnStartScan;

    IBearHandler bearHandler;

    CompositeDisposable subscription = new CompositeDisposable();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bearHandler = ((AdvancedSampleActivity) getActivity()).getBearHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    FlashStatusListener flashListener = new FlashStatusListener() {
        @Override
        public void onStatusChanged(@NonNull FlashStatus flashStatus) {
            Toast.makeText(getActivity(), "FlashStatus = " + flashStatus.name(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStartScan = view.findViewById(R.id.btnStartScan);
        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bearHandler.getCurrentArState() == ArState.IDLE) bearHandler.startScan();
                else if (bearHandler.getCurrentArState() == ArState.SCANNING) bearHandler.stopScan();
            }
        });
        view.findViewById(R.id.btnFlash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bearHandler.isFlashEnabled()) {
                    bearHandler.disableFlash();
                } else {
                    bearHandler.enableFlash();
                }
            }
        });

        subscription.add(bearHandler.getArStateObservingSubject()        //Example how to use arStateObservingSubject for UI changes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArStateChangeRegistrar>() {
                    @Override
                    public void accept(ArStateChangeRegistrar arStateChangeRegistrar) {
                        if (arStateChangeRegistrar.getNewState() == ArState.SCANNING) btnStartScan.setText("Stop Scan");
                        else btnStartScan.setText("Start Scan");
                    }
                }));

        view.findViewById(R.id.btnMarker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bearHandler.getCurrentArState() == ArState.SCANNING) bearHandler.stopScan();
                ((ArActivity) getActivity()).showArSceneWithoutTracking(226620);  //some marker id for example
            }
        });

        dlMain = view.findViewById(R.id.dlMain);
        toolbar = view.findViewById(R.id.toolbar);
        nvMain = view.findViewById(R.id.nvMain);

        toggle = new ActionBarDrawerToggle(getActivity(), dlMain, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dlMain.addDrawerListener(toggle);
        toggle.syncState();
        nvMain.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout = getActivity().getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            if (displayCutout != null)
                setCutoutMargin(view.findViewById(R.id.cutContainer), displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bearHandler.addFlashListener(flashListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        bearHandler.removeFlashListener(flashListener);
    }

    public void closeDrawer() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) dlMain.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_history:
                ((AdvancedSampleActivity) getActivity()).showHistoryScreen();
                break;
            case R.id.nav_example_activity:
                Intent intent = new Intent(getActivity(), ExampleActivity.class);
                startActivity(intent);
                break;
        }
        closeDrawer();
        return false;
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
