package com.ecommpay.msdk.test.android.card

import android.os.Bundle
import android.widget.Toast
import com.ecommpay.msdk.core.domain.interactors.pay.card.sale.SavedCardSaleRequest
import com.ecommpay.msdk.test.android.App
import com.ecommpay.msdk.test.android.PayBaseActivity
import com.ecommpay.msdk.test.android.R

class SavedCardSaleActivity : PayBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_card_sale)

        val savedCards = App.getMsdkSession().getSavedAccounts()
        if (savedCards.isNullOrEmpty()) {
            Toast
                .makeText(applicationContext, "Not found any saved cards", Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }
        progressDialog.show()

        //SavedCardSaleRequest - sale with saved card
        //SavedCardAuthRequest - auth with saved card

        interactor.execute(
            SavedCardSaleRequest(
                cvv = "123",
                accountId = savedCards.first().id
            ),
            this
        )
    }
}