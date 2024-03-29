package com.paymentpage.msdk.core.android.card

import android.os.Bundle
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.interactors.pay.card.sale.NewCardSaleRequest
import com.paymentpage.msdk.core.android.PayBaseActivity
import com.paymentpage.msdk.core.domain.entities.CardDate


class CardSaleActivity : PayBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card_sale)

        progressDialog.show()


        //CardTokenizeRequest - tokenize card
        //CardVerifyRequest - verify card
        //NewCardSaleRequest - sale with card
        //CardSaleTokenizeRequest - sale with previously(CardTokenizeRequest) card token
        //CardAuthRequest - auth with card
        //CardAuthTokenizeRequest - auth with previously(CardTokenizeRequest) card token

        interactor.execute(
            NewCardSaleRequest(
                cvv = "123",
                pan = "5555555555554444",
                expiryDate = CardDate(month = 1, year = 2025),
                cardHolder = "MSDK CORE"
            ),
            this
        )
    }
}