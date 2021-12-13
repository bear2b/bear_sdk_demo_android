package com.bear2b.sampleapp.ui.view.fragments

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

import com.bear2b.sampleapp.R
import kotlinx.android.synthetic.main.webviewfullscreen.*

class WebViewFragment : Fragment() {

    private var url: String? = null

    private lateinit var client : WebChromeClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(URL_PARAM)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.webviewfullscreen, container, false)

    @Nullable
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.clearCache(true)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.setGeolocationDatabasePath(activity?.filesDir?.path)
        initWebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        url?.let{ webView.loadUrl(it) }
    }

    private fun initWebChromeClient() {
        client = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                //TODO: show Alert
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }
        }
        webView.webChromeClient = client
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        client.onHideCustomView()
        webView.onPause()
        webView.stopLoading()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun onDestroyView() {
        webView.destroy()
        super.onDestroyView()
    }

    companion object {
        private const val URL_PARAM : String = "com.bear2b.sampleapp.URL_PARAM"

        fun newInstance(s : String) : Fragment {
            val bundle = Bundle()
            bundle.putString(URL_PARAM, s)
            val fragment = WebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}