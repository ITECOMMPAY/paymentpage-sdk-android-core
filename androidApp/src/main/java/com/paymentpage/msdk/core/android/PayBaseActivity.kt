package com.paymentpage.msdk.core.android

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paymentpage.msdk.core.android.clarification.ClarificationFieldsFragment
import com.paymentpage.msdk.core.android.customer.CustomerFieldFragment
import com.paymentpage.msdk.core.android.final.FinalPageFragment
import com.paymentpage.msdk.core.android.threeDSecure.ThreeDSecurePageFragment
import com.paymentpage.msdk.core.base.ErrorCode
import com.paymentpage.msdk.core.domain.entities.clarification.ClarificationField
import com.paymentpage.msdk.core.domain.entities.customer.CustomerField
import com.paymentpage.msdk.core.domain.entities.payment.Payment
import com.paymentpage.msdk.core.domain.entities.payment.PaymentStatus
import com.paymentpage.msdk.core.domain.entities.threeDSecure.ThreeDSecurePage
import com.paymentpage.msdk.core.domain.interactors.pay.PayDelegate

abstract class PayBaseActivity : AppCompatActivity(), PayDelegate {

    lateinit var progressDialog: ProgressDialog
    internal val interactor = App.getMsdkSession().getPayInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this@PayBaseActivity)
        progressDialog.setMessage("Payment processing")
        progressDialog.setCancelable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.cancel()
        progressDialog.dismiss()
    }

    //Payment is created but status not checked yet
    override fun onPaymentCreated() {
        progressDialog.show()
        Toast.makeText(applicationContext, "Payment created", Toast.LENGTH_SHORT).show()
    }

    //received 3ds page and need open it in WebView
    override fun onThreeDSecure(
        threeDSecurePage: ThreeDSecurePage,
        isCascading: Boolean,
        payment: Payment
    ) {
        progressDialog.dismiss()

        val fragment = ThreeDSecurePageFragment.newInstance(
            threeDSecurePage = threeDSecurePage,
            callback = {
                if (!it.isNullOrEmpty())
                    interactor.threeDSecureRedirectHandle(it)

            })

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "ThreeDSecurePageFragment")
            .commit()
    }

    //need handle and send customer fields
    override fun onCustomerFields(customFields: List<CustomerField>) {
        val fields = customFields.filter { !it.isHidden }

        if (fields.isEmpty()) {
            interactor.sendCustomerFields(emptyList())
            return
        }

        progressDialog.dismiss()
        val fragment = CustomerFieldFragment.newInstance(callback = {
            progressDialog.show()
            interactor.sendCustomerFields(it)
        })
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "CustomerFieldFragment")
            .commit()
        supportFragmentManager.executePendingTransactions()

        fragment.setCustomFields(fields)

    }

    //received clarification fields, which need to fill and send
    override fun onClarificationFields(
        clarificationFields: List<ClarificationField>,
        payment: Payment
    ) {
        if (clarificationFields.isEmpty())
            return

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
        val fragment = FinalPageFragment.newInstance(paymentJson = payment.json ?: "")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "FinalPageFragment")
            .commit()
    }


    override fun onCompleteWithDecline(
        isTryAgain: Boolean,
        paymentMessage: String?,
        payment: Payment
    ) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Payment was declined", Toast.LENGTH_SHORT).show()
        val fragment = FinalPageFragment.newInstance(paymentJson = payment.json ?: "")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "FinalPageFragment")
            .commit()
    }

    override fun onError(code: ErrorCode, message: String) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, code.exceptionName ?: message, Toast.LENGTH_LONG).show()
    }
}