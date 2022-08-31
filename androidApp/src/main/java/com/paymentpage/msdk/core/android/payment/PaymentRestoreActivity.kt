package com.paymentpage.msdk.core.android.payment

import android.app.ProgressDialog
import android.os.Bundle
import com.paymentpage.msdk.core.android.App
import com.paymentpage.msdk.core.android.PayBaseActivity
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.interactors.pay.restore.PaymentRestoreRequest

class PaymentRestoreActivity : PayBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_restore)
        progressDialog = ProgressDialog(this@PaymentRestoreActivity)
        progressDialog.setMessage("Payment restoring")
        progressDialog.setCancelable(false)
        progressDialog.show()

        interactor.execute(
            PaymentRestoreRequest(App.getMsdkSession().getCurrentPayment()?.method ?: ""), this
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.cancel()
    }
}