package com.paymentpage.msdk.core.android.acs

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.entities.threeDSecure.AcsPage


private const val ARG_ACS_PAGE = "acs_page"

class AcsPageFragment : Fragment() {
    private lateinit var acsPage: AcsPage
    private val gson = Gson()
    private var onRedirected: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val json = it.getString(ARG_ACS_PAGE)
            acsPage = gson.fromJson(json, AcsPage::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_acs_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById<WebView>(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url.equals(acsPage.acs?.termUrl)) {
                    onRedirected?.invoke()
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // some code
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler, error: SslError
            ) {
                //  some code
            }
        }

        webView.loadDataWithBaseURL(
            acsPage.acs?.acsUrl,
            acsPage.content ?: "",
            "text/html",
            "utf-8",
            null
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(acsPage: AcsPage, callback: (() -> Unit)) =
            AcsPageFragment().apply {
                onRedirected = callback
                arguments = Bundle().apply {
                    putString(ARG_ACS_PAGE, gson.toJson(acsPage))
                }
            }
    }
}