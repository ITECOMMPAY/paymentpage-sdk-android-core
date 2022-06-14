package com.ecommpay.msdk.test.android.payment

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecommpay.msdk.core.base.ErrorCode
import com.ecommpay.msdk.core.domain.entities.clarification.ClarificationField
import com.ecommpay.msdk.core.domain.entities.payment.Payment
import com.ecommpay.msdk.core.domain.entities.payment.PaymentStatus
import com.ecommpay.msdk.core.domain.entities.threeDSecure.AcsPage
import com.ecommpay.msdk.core.domain.interactors.payment.restore.PaymentRestoreDelegate
import com.ecommpay.msdk.core.domain.interactors.payment.restore.RestorePaymentRequest
import com.ecommpay.msdk.test.android.App
import com.ecommpay.msdk.test.android.R
import com.ecommpay.msdk.test.android.acs.AcsPageFragment
import com.ecommpay.msdk.test.android.clarification.ClarificationFieldsFragment

class PaymentRestoreActivity : AppCompatActivity(), PaymentRestoreDelegate {

    private val interactor = App.getMsdkSession().getPaymentRestoreInteractor()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_restore)
        progressDialog = ProgressDialog(this@PaymentRestoreActivity)
        progressDialog.setMessage("Payment processing")
        progressDialog.setCancelable(false)
        progressDialog.show()

        interactor.execute(RestorePaymentRequest(), this)
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.cancel()
    }

    //received 3ds page and need open it in WebView
    override fun onThreeDSecure(acsPage: AcsPage, isCascading: Boolean, payment: Payment) {
        progressDialog.dismiss()

        val fragment = AcsPageFragment.newInstance(acsPage = acsPage, callback = {
            interactor.threeDSecureHandled()
            progressDialog.show()
        })
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment, "AcsPageFragment")
            .commit()
    }

    //received clarification fields, which need to fill and send
    override fun onClarificationFields(
        clarificationFields: List<ClarificationField>,
        payment: Payment
    ) {
        progressDialog.dismiss()

        val fragment = ClarificationFieldsFragment.newInstance(callback = {
            interactor.sendClarificationFields(it)
            progressDialog.show()
        })
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "ClarificationFieldsFragment")
            .commit()
        supportFragmentManager.executePendingTransactions()

        fragment.setClarificationFields(clarificationFields)
    }

    override fun onStatusChanged(status: PaymentStatus, payment: Payment) {
        Toast.makeText(applicationContext, "Payment status is ${status.name}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCompleteWithSuccess(payment: Payment) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Payment completed with success", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCompleteWithFail(status: String?, payment: Payment) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Payment completed with error", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCompleteWithDecline(payment: Payment) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Payment was declined", Toast.LENGTH_SHORT).show()
    }

    override fun onError(code: ErrorCode, message: String) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, code.exceptionName ?: message, Toast.LENGTH_LONG).show()
    }

}