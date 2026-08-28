package com.jaibreader.app

data class ParsedAmount(
    val amount: Double,
    val currency: String,
    val rawMatch: String
)

object AmountParser {

    private val AMOUNT_REGEX = Regex(
        """(\d[\d,]*(?:\.\d+)?)\s*([\u0600-\u06FF][\u0600-\u06FF.]{0,8})"""
    )

    fun parse(body: String, cutoffWord: String): ParsedAmount? {
        if (body.isBlank()) return null

        val cutoffIndex = if (cutoffWord.isNotBlank()) body.indexOf(cutoffWord) else -1
        val relevantPart = if (cutoffIndex >= 0) body.substring(0, cutoffIndex) else body

        val match = AMOUNT_REGEX.find(relevantPart) ?: return null

        val amountText = match.groupValues[1].replace(",", "")
        val amount = amountText.toDoubleOrNull() ?: return null
        val currency = match.groupValues[2].trim()

        return ParsedAmount(
            amount = amount,
            currency = currency,
            rawMatch = match.value
        )
    }
}
