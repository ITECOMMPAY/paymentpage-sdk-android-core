package com.paymentpage.msdk.core.android.card

import android.os.Bundle
import com.paymentpage.msdk.core.android.PayBaseActivity
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.entities.RecipientInfo
import com.paymentpage.msdk.core.domain.entities.customer.CustomerFieldValue
import com.paymentpage.msdk.core.domain.entities.customer.PaymentFieldValue
import com.paymentpage.msdk.core.domain.entities.customer.SenderFieldValue
import com.paymentpage.msdk.core.domain.interactors.pay.payouts.CardPayoutRequest


class CardPayoutActivity : PayBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payout)

        progressDialog.show()

        interactor.execute(
            CardPayoutRequest(
                pan = "5555555555554444",
                customerFields = listOf(
                    CustomerFieldValue.fromNameWithValue(
                        "first_name",
                        "first_name"
                    )
                ),
                senderFields = listOf(
                    SenderFieldValue.fromNameWithValue("pan", "5555555555554444"),
                    SenderFieldValue.fromNameWithValue("first_name", "senderFirst")
                ),
                recipientInfo = RecipientInfo(firstName = "recipientFirst"),
                paymentFields = listOf(PaymentFieldValue.fromNameWithValue("purpose", "purpose"))

            ),
            this
        )
    }
}