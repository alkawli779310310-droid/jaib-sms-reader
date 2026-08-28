package com.jaibreader.app

import android.content.Context

object SettingsStore {

    private const val PREFS_NAME = "jaib_reader_prefs"

    private const val KEY_SENDER_FILTER = "sender_filter"
    private const val KEY_SHEET_WEBHOOK_URL = "sheet_webhook_url"
    private const val KEY_CUTOFF_WORD = "cutoff_word"

    private const val DEFAULT_SENDER_FILTER = "Jaib"
    private const val DEFAULT_CUTOFF_WORD = "رص"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSenderFilter(context: Context): String =
        prefs(context).getString(KEY_SENDER_FILTER, DEFAULT_SENDER_FILTER) ?: DEFAULT_SENDER_FILTER

    fun setSenderFilter(context: Context, value: String) {
        prefs(context).edit().putString(KEY_SENDER_FILTER, value).apply()
    }

    fun getSheetWebhookUrl(context: Context): String =
        prefs(context).getString(KEY_SHEET_WEBHOOK_URL, "") ?: ""

    fun setSheetWebhookUrl(context: Context, value: String) {
        prefs(context).edit().putString(KEY_SHEET_WEBHOOK_URL, value).apply()
    }

    fun getCutoffWord(context: Context): String =
        prefs(context).getString(KEY_CUTOFF_WORD, DEFAULT_CUTOFF_WORD) ?: DEFAULT_CUTOFF_WORD

    fun setCutoffWord(context: Context, value: String) {
        prefs(context).edit().putString(KEY_CUTOFF_WORD, value).apply()
    }
}
