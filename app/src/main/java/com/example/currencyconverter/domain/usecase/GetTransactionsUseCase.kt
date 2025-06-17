package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.model.Transaction
import com.example.currencyconverter.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): List<Transaction> {
        return transactionRepository.getAllTransactions()
    }
}