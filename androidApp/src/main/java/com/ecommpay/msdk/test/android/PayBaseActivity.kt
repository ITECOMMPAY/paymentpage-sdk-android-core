package com.ecommpay.msdk.test.android

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecommpay.msdk.core.base.ErrorCode
import com.ecommpay.msdk.core.domain.entities.clarification.ClarificationField
import com.ecommpay.msdk.core.domain.entities.customer.CustomerField
import com.ecommpay.msdk.core.domain.entities.payment.Payment
import com.ecommpay.msdk.core.domain.entities.payment.PaymentStatus
import com.ecommpay.msdk.core.domain.entities.threeDSecure.AcsPage
import com.ecommpay.msdk.core.domain.interactors.pay.PayDelegate
import com.ecommpay.msdk.test.android.acs.AcsPageFragment
import com.ecommpay.msdk.test.android.clarification.ClarificationFieldsFragment
import com.ecommpay.msdk.test.android.customer.CustomerFieldFragment

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

    //need handle and send customer fields
    override fun onCustomerFields(customFields: List<CustomerField>) {

        progressDialog.dismiss()
        val fragment = CustomerFieldFragment.newInstance(callback = {
            interactor.sendCustomerFields(it)
            progressDialog.show()
        })
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "CustomerFieldFragment")
            .commit()
        supportFragmentManager.executePendingTransactions()

        fragment.setCustomFields(customFields)

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