package com.paymentpage.msdk.core.android.clarification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paymentpage.msdk.core.android.R
import com.paymentpage.msdk.core.domain.entities.clarification.ClarificationField
import com.paymentpage.msdk.core.domain.entities.clarification.ClarificationFieldValue
import com.paymentpage.msdk.core.android.clarification.adapter.ClarificationFieldsAdapter


class ClarificationFieldsFragment : Fragment() {
    private var onCompleted: ((List<ClarificationFieldValue>) -> Unit)? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClarificationFieldsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clarification_fields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.clarificationFields)

        view.findViewById<Button>(R.id.applyFields).setOnClickListener {
            onCompleted?.invoke(adapter.getFields())
        }

    }

    fun setClarificationFields(clarificationFields: List<ClarificationField>) {
        adapter = ClarificationFieldsAdapter(clarificationFields)
        recyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(callback: ((List<ClarificationFieldValue>) -> Unit)) =
            ClarificationFieldsFragment().apply { onCompleted = callback }
    }
}