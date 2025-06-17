package com.example.currencyconverter.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Int = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val fromAmount: Double,
    val toAmount: Double,
    val dateTime: LocalDateTime
)
