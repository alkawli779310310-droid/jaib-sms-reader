package com.jaibreader.app

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object SheetUploader {

    private val client = OkHttpClient()

    fun upload(
        webhookUrl: String,
        amount: Double,
        currency: String,
        senderInfo: String
    ) {
        if (webhookUrl.isBlank()) {
            Log.w("SheetUploader", "لم يتم ضبط رابط جدول البيانات بعد")
            return
        }

        val formBody = FormBody.Builder()
            .add("amount", amount.toString())
            .add("currency", currency)
            .add("sender", senderInfo)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()

        val request = Request.Builder()
            .url(webhookUrl)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SheetUploader", "فشل الإرسال إلى جدول البيانات", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.close()
            }
        })
    }
}
