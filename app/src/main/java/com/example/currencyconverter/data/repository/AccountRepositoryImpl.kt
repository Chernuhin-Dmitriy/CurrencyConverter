package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.model.Account
import com.example.currencyconverter.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun getAllAccounts(): List<Account> {
        return accountDao.getAll().map { it.toDomain() }
    }

    override fun getAllAccountsFlow(): Flow<List<Account>> {
        return accountDao.getAllAsFlow().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override suspend fun updateAccounts(accounts: List<Account>) {
        accountDao.insertAll(*accounts.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun initializeDefaultAccount() {
        val existingAccounts = accountDao.getAll()
        if (existingAccounts.isEmpty()) {
            accountDao.insertAll(AccountDbo("RUB", 75000.0))
        }
    }

    private fun AccountDbo.toDomain() = Account(
        currencyCode = code,
        amount = amount
    )

    private fun Account.toEntity() = AccountDbo(
        code = currencyCode,
        amount = amount
    )
}