package com.paymentpage.msdk.core.android

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paymentpage.msdk.core.android.account.remove.AccountRemoveActivity
import com.paymentpage.msdk.core.android.aps.ApsActivity
import com.paymentpage.msdk.core.android.card.CardSaleActivity
import com.paymentpage.msdk.core.android.card.SavedCardSaleActivity
import com.paymentpage.msdk.core.android.gpay.GPaySaleActivity
import com.paymentpage.msdk.core.android.payment.PaymentRestoreActivity
import com.paymentpage.msdk.core.base.ErrorCode
import com.paymentpage.msdk.core.domain.entities.PaymentInfo
import com.paymentpage.msdk.core.domain.entities.init.PaymentMethod
import com.paymentpage.msdk.core.domain.entities.init.SavedAccount
import com.paymentpage.msdk.core.domain.entities.payment.Payment
import com.paymentpage.msdk.core.domain.interactors.init.InitDelegate
import com.paymentpage.msdk.core.domain.interactors.init.InitInteractor
import com.paymentpage.msdk.core.domain.interactors.init.InitRequest

class MainActivity : AppCompatActivity(), InitDelegate {

    private lateinit var progressDialog: ProgressDialog
    private val interactor: InitInteractor = App.getMsdkSession().getInitInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Payment init")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //create payment info
        val paymentInfo = PaymentInfo(
            projectId = BuildConfig.PROJECT_ID,
            paymentId = CommonUtils.getRandomPaymentId(),
            paymentAmount = 7002,
            paymentCurrency = "USD",
            //set customer id if needed
            customerId = "12",
            //set token if need create sale with token
            //token = "TOKEN"
        )
        //calculate signature
        paymentInfo.signature =
            SignatureGenerator.generateSignature(
                paymentInfo.getParamsForSignature(),
                BuildConfig.PROJECT_SECRET_KEY
            )

        //payment init
        interactor.execute(
            InitRequest(
                paymentInfo = paymentInfo,
                recurrentInfo = null,
                additionalFields = emptyList() //you can set there your custom fields
            ),
            this
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.cancel()
        progressDialog.dismiss()
    }

    //received init data
    override fun onInitReceived(
        paymentMethods: List<PaymentMethod>,
        savedAccounts: List<SavedAccount>
    ) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Init Success Received", Toast.LENGTH_SHORT).show()

        //get string resource manager for server side strings
        val stringResourceManager = App.getMsdkSession().getStringResourceManager()
        //for example get methods list title string override
        val title = stringResourceManager.getStringByKey("title_payment_methods")

        //get icons resource manager
        val setSecureLogoResourceManager = App.getMsdkSession().getSecureLogoResourceManager()
        //get all logo keys
        val keys = setSecureLogoResourceManager.allKeys
        //get specific logo
        val visaIconUrl = setSecureLogoResourceManager.getLogoUrl("visa")

        findViewById<Button>(R.id.gPaySale).isEnabled = true
        findViewById<Button>(R.id.newCard).isEnabled = true
        findViewById<Button>(R.id.savedCard).isEnabled = true
        findViewById<Button>(R.id.removeSavedCard).isEnabled = true
        findViewById<Button>(R.id.aps).isEnabled = true
    }

    //received already created payment from init
    override fun onPaymentRestored(payment: Payment) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, "Payment received", Toast.LENGTH_LONG).show()

        findViewById<Button>(R.id.paymentRestore).isEnabled = true
        findViewById<Button>(R.id.gPaySale).isEnabled = false
        findViewById<Button>(R.id.newCard).isEnabled = false
        findViewById<Button>(R.id.savedCard).isEnabled = false
        findViewById<Button>(R.id.removeSavedCard).isEnabled = false
        findViewById<Button>(R.id.aps).isEnabled = false
    }

    override fun onError(code: ErrorCode, message: String) {
        progressDialog.dismiss()
        Toast.makeText(applicationContext, code.exceptionName ?: message, Toast.LENGTH_LONG).show()
    }

    fun removeSavedCard(view: View?) {
        finish()
        startActivity(Intent(this, AccountRemoveActivity::class.java))
    }

    fun newCard(view: View?) {
        finish()
        startActivity(Intent(this, CardSaleActivity::class.java))
    }

    fun savedCard(view: View?) {
        finish()
        startActivity(Intent(this, SavedCardSaleActivity::class.java))
    }

    fun gPaySale(view: View?) {
        finish()
        startActivity(Intent(this, GPaySaleActivity::class.java))

    }

    fun paymentRestore(view: View?) {
        finish()
        startActivity(Intent(this, PaymentRestoreActivity::class.java))
    }

    fun aps(view: View?) {
        finish()
        startActivity(Intent(this, ApsActivity::class.java))
    }

}
