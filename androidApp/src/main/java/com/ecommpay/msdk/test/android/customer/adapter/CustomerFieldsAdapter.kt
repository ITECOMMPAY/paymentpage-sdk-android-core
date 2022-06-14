@file:Suppress("UnnecessaryVariable", "RemoveSingleExpressionStringTemplate")

package com.ecommpay.msdk.test.android.customer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.ecommpay.msdk.core.domain.entities.customer.CustomerField
import com.ecommpay.msdk.core.domain.entities.customer.CustomerFieldValue
import com.ecommpay.msdk.test.android.R


class CustomerFieldsAdapter(private val customerFields: List<CustomerField>) :
    RecyclerView.Adapter<CustomerFieldsAdapter.ViewHolder>() {

    private val customerFieldsValues = mutableMapOf<String, String?>()

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
        val field = customerFields[position]

        holder.title.text = field.label + "${if (field.isRequired) " [required]" else ""}"
        holder.value.hint = field.hint

        holder.value.addTextChangedListener {
            val value = it.toString()
            if (field.validator?.isValid(value) != false) {
                customerFieldsValues[field.serverName] = value
            } else {
                holder.value.error = field.errorMessage ?: "Invalid value"
            }

        }
    }

    override fun getItemCount() = customerFields.size

    fun getFields() =
        customerFieldsValues
            .filterValues { !it.isNullOrEmpty() }
            .map { CustomerFieldValue.fromNameWithValue(it.key, it.value!!) }.toList()
}