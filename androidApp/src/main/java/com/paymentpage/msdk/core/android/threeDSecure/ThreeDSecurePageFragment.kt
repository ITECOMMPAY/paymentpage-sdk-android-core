package com.paymentpage.msdk.core.android.threeDSecure

import android.app.ProgressDialog
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
import com.paymentpage.msdk.core.domain.entities.threeDSecure.ThreeDSecurePage
import com.paymentpage.msdk.core.domain.entities.threeDSecure.ThreeDSecurePageType


private const val ARG_THREE_D_SECURE_PAGE = "3d_secure_page"

class ThreeDSecurePageFragment : Fragment() {

    private lateinit var threeDSecurePage: ThreeDSecurePage
    private val gson = Gson()
    private var onRedirected: ((String?) -> Unit)? = null

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        arguments?.let {
            val json = it.getString(ARG_THREE_D_SECURE_PAGE)
            threeDSecurePage = gson.fromJson(json, ThreeDSecurePage::class.java)
        }

        if (threeDSecurePage.type == ThreeDSecurePageType.THREE_DS_2_FRICTIONLESS) {
            progressDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog.dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_3ds_page, container, false)
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
                onRedirected?.invoke(url)
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
            threeDSecurePage.loadUrl,
            threeDSecurePage.content ?: "",
            "text/html",
            "utf-8",
            null
        )

    }

    companion object {
        @JvmStatic
        fun newInstance(threeDSecurePage: ThreeDSecurePage, callback: ((String?) -> Unit)) =
            ThreeDSecurePageFragment().apply {
                onRedirected = callback
                arguments = Bundle().apply {
                    putString(ARG_THREE_D_SECURE_PAGE, gson.toJson(threeDSecurePage))
                }
            }
    }
}