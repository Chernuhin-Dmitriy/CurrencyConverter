package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.model.Account
import com.example.currencyconverter.domain.model.Transaction
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.TransactionRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ExchangeCurrencyUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        fromCurrency: String,
        toCurrency: String,
        fromAmount: Double,
        toAmount: Double
    ) {
        // Сохраняем транзакцию
        val transaction = Transaction(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            fromAmount = fromAmount,
            toAmount = toAmount,
            dateTime = LocalDateTime.now()
        )
        transactionRepository.insertTransaction(transaction)

        // Обновляем балансы
        val accounts = accountRepository.getAllAccounts().toMutableList()
        val fromAccount = accounts.find { it.currencyCode == fromCurrency }
        val toAccount = accounts.find { it.currencyCode == toCurrency }

        accounts.removeAll { it.currencyCode == fromCurrency || it.currencyCode == toCurrency }

        if (fromAccount != null) {
            accounts.add(fromAccount.copy(amount = fromAccount.amount - fromAmount))
        }

        if (toAccount != null) {
            accounts.add(toAccount.copy(amount = toAccount.amount + toAmount))
        } else {
            accounts.add(Account(toCurrency, toAmount))
        }

        accountRepository.updateAccounts(accounts)
    }
}