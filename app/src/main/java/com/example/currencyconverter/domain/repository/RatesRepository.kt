package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.model.Rate

interface RatesRepository {
    suspend fun getRates(baseCurrencyCode: String, amount: Double): List<Rate>
}