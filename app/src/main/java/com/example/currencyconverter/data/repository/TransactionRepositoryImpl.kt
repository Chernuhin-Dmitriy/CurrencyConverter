package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.model.Transaction
import com.example.currencyconverter.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionDao.getAll().map { it.toDomain() }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertAll(transaction.toEntity())
    }

    private fun TransactionDbo.toDomain() = Transaction(
        id = id,
        fromCurrency = from,
        toCurrency = to,
        fromAmount = fromAmount,
        toAmount = toAmount,
        dateTime = dateTime
    )

    private fun Transaction.toEntity() = TransactionDbo(
        id = id,
        from = fromCurrency,
        to = toCurrency,
        fromAmount = fromAmount,
        toAmount = toAmount,
        dateTime = dateTime
    )
}