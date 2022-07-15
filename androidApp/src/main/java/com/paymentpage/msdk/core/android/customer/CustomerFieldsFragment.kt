package com.paymentpage.msdk.core.android.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.entities.customer.CustomerField
import com.paymentpage.msdk.core.domain.entities.customer.CustomerFieldValue
import com.paymentpage.msdk.core.android.customer.adapter.CustomerFieldsAdapter


class CustomerFieldFragment : Fragment() {
    private var onCompleted: ((List<CustomerFieldValue>) -> Unit)? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomerFieldsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_fields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.customerFields)

        view.findViewById<Button>(R.id.applyFields).setOnClickListener {
            onCompleted?.invoke(adapter.getFields())
        }

    }

    fun setCustomFields(customerFields: List<CustomerField>) {
        adapter = CustomerFieldsAdapter(customerFields.filter { !it.isHidden })
        recyclerView.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance(callback: ((List<CustomerFieldValue>) -> Unit)) =
            CustomerFieldFragment().apply { onCompleted = callback }
    }
}