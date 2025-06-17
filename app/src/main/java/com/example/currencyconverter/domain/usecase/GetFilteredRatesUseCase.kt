package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.model.Rate
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import javax.inject.Inject

class GetFilteredRatesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        baseCurrencyCode: String,
        desiredAmount: Double
    ): List<Rate> {
        val rates = ratesRepository.getRates(baseCurrencyCode, 1.0)
        val accounts = accountRepository.getAllAccounts().associateBy { it.currencyCode }

        return rates.mapNotNull { rate ->
            val accountBalance = accounts[rate.currencyCode]?.amount ?: 0.0
            val requiredAmount = desiredAmount * rate.value

            if (rate.currencyCode == baseCurrencyCode || accountBalance >= requiredAmount) {
                rate.copy(
                    value = if (rate.currencyCode == baseCurrencyCode) desiredAmount else requiredAmount,
                    accountBalance = accountBalance
                )
            } else null
        }
    }
}