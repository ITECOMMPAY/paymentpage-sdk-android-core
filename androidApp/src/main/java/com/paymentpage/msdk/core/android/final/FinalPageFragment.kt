package com.paymentpage.msdk.core.android.final

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.paymentpage.msdk.core.android.R


private const val ARG_PAYMENT_JSON = "payment_json"

class FinalPageFragment : Fragment() {

    private lateinit var paymentJson: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paymentJson = it.getString(ARG_PAYMENT_JSON) ?: ""
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_final_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val payment = view.findViewById<TextView>(R.id.payment)
        payment.text = paymentJson

    }

    companion object {
        @JvmStatic
        fun newInstance(paymentJson: String) =
            FinalPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PAYMENT_JSON, paymentJson)
                }
            }
    }
}