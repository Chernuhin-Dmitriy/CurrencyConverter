package com.example.currencyconverter.data.dataSource.room.account.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg accounts: AccountDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountDbo)

    @Update
    suspend fun update(account: AccountDbo)

    @Query("SELECT * FROM accounts")
    suspend fun getAll(): List<AccountDbo>

    @Query("SELECT * FROM accounts")
    fun getAllAsFlow(): Flow<List<AccountDbo>>

    @Query("SELECT * FROM accounts WHERE currency_code = :currencyCode")
    suspend fun getAccountByCurrency(currencyCode: String): AccountDbo?

    @Query("DELETE FROM accounts WHERE currency_code = :currencyCode")
    suspend fun deleteAccountByCurrency(currencyCode: String)

    @Query("SELECT amount FROM accounts WHERE currency_code = :currencyCode")
    suspend fun getBalanceByCurrency(currencyCode: String): Double?
}