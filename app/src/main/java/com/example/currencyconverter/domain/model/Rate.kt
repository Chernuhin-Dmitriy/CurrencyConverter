package com.example.currencyconverter.domain.model

data class Rate(
    val currencyCode: String,
    val value: Double,
    val accountBalance: Double? = null
)