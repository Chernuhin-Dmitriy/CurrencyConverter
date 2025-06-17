package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.domain.model.Rate
import com.example.currencyconverter.domain.repository.RatesRepository
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val ratesService: RatesService
) : RatesRepository {

    override suspend fun getRates(baseCurrencyCode: String, amount: Double): List<Rate> {
        return ratesService.getRates(baseCurrencyCode, amount).map { dto ->
            Rate(
                currencyCode = dto.currency,
                value = dto.value
            )
        }
    }
}