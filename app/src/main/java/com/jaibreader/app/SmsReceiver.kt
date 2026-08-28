package com.jaibreader.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val senderFilter = SettingsStore.getSenderFilter(context)
        val cutoffWord = SettingsStore.getCutoffWord(context)
        val webhookUrl = SettingsStore.getSheetWebhookUrl(context)

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (msg in messages) {
            val sender = msg.originatingAddress ?: ""
            val body = msg.messageBody ?: ""

            if (senderFilter.isNotBlank() && !sender.contains(senderFilter, ignoreCase = true)) {
                continue
            }

            val parsed = AmountParser.parse(body, cutoffWord) ?: continue

            Toast.makeText(
                context,
                "تم استلام: ${parsed.amount} ${parsed.currency}",
                Toast.LENGTH_LONG
            ).show()

            SheetUploader.upload(
                webhookUrl = webhookUrl,
                amount = parsed.amount,
                currency = parsed.currency,
                senderInfo = sender
            )
        }
    }
}
