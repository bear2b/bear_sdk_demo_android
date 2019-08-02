package com.bear2b.sampleapp.ui.view.fragments;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bear2b.sampleapp.R;

public class WebViewFragment extends Fragment {
    String url;

    WebChromeClient client;

    WebView webView;
    private static final String URL_PARAM = "com.bear2b.sampleapp.URL_PARAM";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(URL_PARAM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webviewfullscreen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setGeolocationDatabasePath(getActivity().getFilesDir().getPath());
        initWebChromeClient();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);

    }

    private void initWebChromeClient() {
        client = new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                //TODO: show Alert
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        };
        webView.setWebChromeClient(client);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        client.onHideCustomView();
        webView.onPause();
        webView.stopLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onDestroyView() {
        webView.destroy();
        super.onDestroyView();
    }

    public static Fragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(URL_PARAM, s);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}