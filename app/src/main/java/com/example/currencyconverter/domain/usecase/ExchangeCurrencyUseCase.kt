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
        // Получаем текущие балансы
        val accounts = accountRepository.getAllAccounts().toMutableList()

        // Находим счет валюты, которую продаем
        val fromAccountIndex = accounts.indexOfFirst { it.currencyCode == fromCurrency }
        if (fromAccountIndex == -1) {
            throw IllegalStateException("Счет валюты $fromCurrency не найден")
        }

        val fromAccount = accounts[fromAccountIndex]

        // Проверяем, достаточно ли средств
        if (fromAccount.amount < fromAmount) {
            throw IllegalStateException("Недостаточно средств для обмена. Доступно: ${fromAccount.amount}, требуется: $fromAmount")
        }

        // Обновляем баланс валюты, которую продаем (списываем)
        accounts[fromAccountIndex] = fromAccount.copy(amount = fromAccount.amount - fromAmount)

        // Находим или создаем счет валюты, которую покупаем
        val toAccountIndex = accounts.indexOfFirst { it.currencyCode == toCurrency }
        if (toAccountIndex != -1) {
            // Счет существует - добавляем к существующему балансу
            val toAccount = accounts[toAccountIndex]
            accounts[toAccountIndex] = toAccount.copy(amount = toAccount.amount + toAmount)
        } else {
            // Счет не существует - создаем новый
            accounts.add(Account(currencyCode = toCurrency, amount = toAmount))
        }

        // Сохраняем обновленные балансы
        accountRepository.updateAccounts(accounts)

        // Сохраняем транзакцию
        val transaction = Transaction(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            fromAmount = fromAmount,
            toAmount = toAmount,
            dateTime = LocalDateTime.now()
        )
        transactionRepository.insertTransaction(transaction)
    }
}