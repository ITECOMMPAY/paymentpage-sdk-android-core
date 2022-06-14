package com.ecommpay.msdk.test.android.account.remove

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecommpay.msdk.core.base.ErrorCode
import com.ecommpay.msdk.core.domain.interactors.card.remove.CardRemoveDelegate
import com.ecommpay.msdk.core.domain.interactors.card.remove.CardRemoveRequest
import com.ecommpay.msdk.test.android.App
import com.ecommpay.msdk.test.android.R

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