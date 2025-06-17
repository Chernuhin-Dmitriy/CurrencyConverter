package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun getAllAccounts(): List<Account>
    fun getAllAccountsFlow(): Flow<List<Account>>
    suspend fun updateAccounts(accounts: List<Account>)
    suspend fun initializeDefaultAccount()
}