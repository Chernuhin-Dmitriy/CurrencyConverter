package com.example.currencyconverter.domain.usecase


import com.example.currencyconverter.domain.model.Rate
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import javax.inject.Inject

class GetRatesWithBalanceUseCase @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(baseCurrencyCode: String, amount: Double): List<Rate> {
        val rates = ratesRepository.getRates(baseCurrencyCode, amount)
        val accounts = accountRepository.getAllAccounts().associateBy { it.currencyCode }

        return rates.map { rate ->
            rate.copy(accountBalance = accounts[rate.currencyCode]?.amount)
        }
    }
}