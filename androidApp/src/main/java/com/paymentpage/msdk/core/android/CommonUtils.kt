package com.paymentpage.msdk.core.android

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object CommonUtils {
    fun getRandomPaymentId(): String {
        val randomNumber = Random().nextInt(9999) + 1000
        return "msdk_core_payment_id_$randomNumber"
    }
}

inline fun <reified T> Gson.fromJsonTypeToken(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)