package com.paymentpage.msdk.core.android.account.remove

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.base.ErrorCode
import com.paymentpage.msdk.core.domain.interactors.card.remove.CardRemoveDelegate
import com.paymentpage.msdk.core.domain.interactors.card.remove.CardRemoveRequest
import com.paymentpage.msdk.core.android.App

class AccountRemoveActivity : AppCompatActivity(), CardRemoveDelegate {

    private lateinit var progressDialog: ProgressDialog

    private val interactor = App.getMsdkSession().getCardRemoveInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_remove)

        progressDialog = ProgressDialog(this@AccountRemoveActivity)
        progressDialog.setMessage("Removing saved card")
        progressDialog.setCancelable(false)
        progressDialog.show()

        interactor.execute(CardRemoveRequest(1), this)
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.cancel()
        progressDialog.dismiss()
    }

    override fun onSuccess(result: Boolean) {
        progressDialog.dismiss()
        Toast.makeText(
            applicationContext,
            if (result) "Card removed" else "Card does not removed",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onError(code: ErrorCode, message: String) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, code.exceptionName ?: message, Toast.LENGTH_LONG).show()
    }
}