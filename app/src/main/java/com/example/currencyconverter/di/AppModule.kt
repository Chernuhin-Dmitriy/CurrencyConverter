package com.example.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.repository.AccountRepositoryImpl
import com.example.currencyconverter.data.repository.RatesRepositoryImpl
import com.example.currencyconverter.data.repository.TransactionRepositoryImpl
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import com.example.currencyconverter.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ConverterDatabase {
        return Room.databaseBuilder(
            context,
            ConverterDatabase::class.java,
            "converter_database"
        ).build()
    }

    @Provides
    fun provideAccountDao(database: ConverterDatabase) = database.accountDao()

    @Provides
    fun provideTransactionDao(database: ConverterDatabase) = database.transactionDao()

    @Provides
    @Singleton
    fun provideRatesService(): RatesService = RemoteRatesServiceImpl()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    abstract fun bindRatesRepository(
        ratesRepositoryImpl: RatesRepositoryImpl
    ): RatesRepository
}