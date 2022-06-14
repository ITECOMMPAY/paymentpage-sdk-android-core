@file:Suppress("UnnecessaryVariable", "RemoveSingleExpressionStringTemplate")

package com.ecommpay.msdk.test.android.clarification.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.ecommpay.msdk.core.domain.entities.clarification.ClarificationField
import com.ecommpay.msdk.core.domain.entities.clarification.ClarificationFieldValue
import com.ecommpay.msdk.test.android.R


class ClarificationFieldsAdapter(private val clarificationFields: List<ClarificationField>) :
    RecyclerView.Adapter<ClarificationFieldsAdapter.ViewHolder>() {

    private val clarificationFieldValues = mutableMapOf<String, String?>()

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val value: EditText = view.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_customer_field_item, parent, false)

        val viewHolder = ViewHolder(itemView)
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = clarificationFields[position]

        holder.title.text = field.defaultLabel ?: field.serverName
        holder.value.hint = field.defaultHint

        holder.value.addTextChangedListener {
            val value = it.toString()
            if (field.validator?.isValid(value) != false) {
                clarificationFieldValues[field.serverName] = value
            } else {
                holder.value.error = field.defaultErrorMessage ?: "Invalid value"
            }

        }
    }

    override fun getItemCount() = clarificationFields.size

    fun getFields() =
        clarificationFieldValues
            .filterValues { !it.isNullOrEmpty() }
            .map { ClarificationFieldValue.fromNameWithValue(it.key, it.value!!) }
            .toList()
}