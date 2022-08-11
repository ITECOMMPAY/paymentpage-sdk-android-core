package com.paymentpage.msdk.core.android.aps

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View.GONE
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.paymentpage.msdk.core.android.App
import com.paymentpage.msdk.core.android.PayBaseActivity
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.entities.init.PaymentMethodType
import com.paymentpage.msdk.core.domain.entities.payment.Payment
import com.paymentpage.msdk.core.domain.interactors.pay.aps.ApsSaleRequest

class ApsActivity : PayBaseActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aps)

        val apsMethod = App.getMsdkSession().getPaymentMethods()
            ?.lastOrNull { it.type == PaymentMethodType.WEBMONEY_LIGHT }
        val paymentUrl = apsMethod?.paymentUrl

        webView = findViewById(R.id.webView)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url != paymentUrl && apsMethod != null) {
                    interactor.execute(
                        ApsSaleRequest(apsMethod.type.value),
                        this@ApsActivity
                    )
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

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true

        if (!paymentUrl.isNullOrEmpty()) {
            webView.loadUrl(paymentUrl)
        }
    }

    override fun onCompleteWithDecline(paymentMessage: String?, payment: Payment) {
        super.onCompleteWithDecline(paymentMessage, payment)
        webView.visibility = GONE
    }

    override fun onCompleteWithFail(
        isTryAgain: Boolean,
        paymentMessage: String?,
        payment: Payment
    ) {
        super.onCompleteWithFail(isTryAgain, paymentMessage, payment)

        webView.visibility = GONE
    }

    override fun onCompleteWithSuccess(payment: Payment) {
        super.onCompleteWithSuccess(payment)
        webView.visibility = GONE

    }
}