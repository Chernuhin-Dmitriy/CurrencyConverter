package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.model.Transaction

interface TransactionRepository {
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
}