package com.paymentpage.msdk.core.android.gpay

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.paymentpage.msdk.core.android.BuildConfig
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.interactors.pay.googlePay.GooglePayEnvironment
import com.paymentpage.msdk.core.domain.interactors.pay.googlePay.GooglePaySaleRequest
import com.paymentpage.msdk.core.googlePay.GooglePayHelper
import com.paymentpage.msdk.core.android.App
import com.paymentpage.msdk.core.android.PayBaseActivity
import org.json.JSONObject
import java.math.BigDecimal


class GPaySaleActivity : PayBaseActivity() {

    private val googlePayHelper = GooglePayHelper(BuildConfig.GPAY_MERCHANT_ID, "Example Merchant")
    private lateinit var client: PaymentsClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpay_sale)

        val request =
            IsReadyToPayRequest.fromJson(googlePayHelper.createReadyToPayRequest().toString())

        progressDialog = ProgressDialog(this@GPaySaleActivity)
        progressDialog.setMessage("Gpay sale processing")
        progressDialog.setCancelable(false)

        client = Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)//test
                .setTheme(WalletConstants.THEME_LIGHT)
                .build()
        )

        client.isReadyToPay(request).addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {

            val googleJson =
                googlePayHelper.createPaymentDataRequest(
                    BigDecimal.valueOf(
                        (App.getMsdkSession().getPaymentInfo()?.paymentAmount ?: 0) / 100
                    ), App.getMsdkSession().getPaymentInfo()?.paymentCurrency ?: "USD"
                ).toString()
            val gpayRequest = PaymentDataRequest.fromJson(googleJson)

            AutoResolveHelper.resolveTask(
                client.loadPaymentData(gpayRequest),
                this,
                991
            )
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        val paymentData = PaymentData.getFromIntent(data)
        val paymentInformation = paymentData?.toJson() ?: return
        val paymentMethodData: JSONObject =
            JSONObject(paymentInformation).getJSONObject("paymentMethodData")
        val token = paymentMethodData.getJSONObject("tokenizationData").getString("token")

        progressDialog.show()
        interactor.execute(
            GooglePaySaleRequest(
                merchantId = BuildConfig.GPAY_MERCHANT_ID,
                token = token,
                environment = GooglePayEnvironment.TEST
            ),
            this
        )
    }
}