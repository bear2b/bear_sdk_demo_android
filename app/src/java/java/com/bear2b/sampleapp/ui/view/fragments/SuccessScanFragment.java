package com.bear2b.sampleapp.ui.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bear.common.sdk.IBearHandler;
import com.bear2b.sampleapp.R;
import com.bear2b.sampleapp.ui.view.activities.AdvancedSampleActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SuccessScanFragment extends Fragment {

    private IBearHandler bearHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bearHandler = ((AdvancedSampleActivity) getActivity()).getBearHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.success_scan_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnScreenshot = view.findViewById(R.id.btnScreenshot);
        btnScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuccessScanFragmentPermissionsDispatcher.shareImageWithCheck(SuccessScanFragment.this);
            }
        });
        Button btnSamplePausingFragment = view.findViewById(R.id.btnSamplePausingFragment);
        btnSamplePausingFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AdvancedSampleActivity) getActivity()).pauseArView();
                ((AdvancedSampleActivity) getActivity()).showSamplePausingFragment();
            }
        });
    }

    @Override
    public void onPause() {
        // Screenshot may not be completed when fragment is paused
        bearHandler.cancelScreenshot();
        super.onPause();
    }

    public static SuccessScanFragment newInstance() {
        return new SuccessScanFragment();
    }

    @NeedsPermission({"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})
    public void shareImage() {
        bearHandler.takeScreenshot(new IBearHandler.ScreenshotCallback() {
            @Override
            public void onComplete(@NonNull Uri imageUri) {
                ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setType("image/*")
                        .setStream(imageUri)
                        .startChooser();
            }

            @Override
            public void onCancel() {
                Log.e("DEV", "Screenshot has been cancelled");
            }
        });
    }

    @OnShowRationale({"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setMessage("request permission")
                .setPositiveButton("allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})
    void showDeniedStorage() {
        Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"})
    void showNeverAskForStorage() {
        Toast.makeText(getActivity(), "never ask again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SuccessScanFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
