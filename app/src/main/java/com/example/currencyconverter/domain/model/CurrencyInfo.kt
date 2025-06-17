package com.example.currencyconverter.domain.model

data class CurrencyInfo(
    val code: String,
    val name: String,
    val symbol: String,
    val flagResourceId: Int? = null
)
