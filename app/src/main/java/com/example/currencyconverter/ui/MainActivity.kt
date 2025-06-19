package com.example.currencyconverter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.presentation.CurrenciesScreen
import com.example.currencyconverter.presentation.CurrenciesViewModel
import com.example.currencyconverter.presentation.ExchangeScreen
import com.example.currencyconverter.presentation.ExchangeViewModel
import com.example.currencyconverter.presentation.TransactionsScreen
import com.example.currencyconverter.presentation.TransactionsViewModel
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var accountRepository: AccountRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CurrencyConverterTheme {
                val navController = rememberNavController()

                // Инициализация дефолтного аккаунта при запуске
                LaunchedEffect(Unit) {
                    accountRepository.initializeDefaultAccount()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "currencies",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("currencies") {
                            val viewModel: CurrenciesViewModel = hiltViewModel()
                            CurrenciesScreen(
                                viewModel = viewModel,
                                onNavigateToExchange = { fromCurrency, toCurrency, fromAmount, toAmount ->
                                    navController.navigate("exchange/$fromCurrency/$toCurrency/$fromAmount/$toAmount")
                                },
                                onNavigateToTransactions = {
                                    navController.navigate("transactions")
                                }
                            )
                        }

                        composable("exchange/{fromCurrency}/{toCurrency}/{fromAmount}/{toAmount}") { backStackEntry ->
                            val fromCurrency = backStackEntry.arguments?.getString("fromCurrency") ?: ""
                            val toCurrency = backStackEntry.arguments?.getString("toCurrency") ?: ""
                            val fromAmount = backStackEntry.arguments?.getString("fromAmount")?.toDoubleOrNull() ?: 0.0
                            val toAmount = backStackEntry.arguments?.getString("toAmount")?.toDoubleOrNull() ?: 0.0

                            val viewModel: ExchangeViewModel = hiltViewModel()
                            ExchangeScreen(
                                viewModel = viewModel,
                                fromCurrency = fromCurrency,
                                toCurrency = toCurrency,
                                fromAmount = fromAmount,
                                toAmount = toAmount,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("transactions") {
                            val viewModel: TransactionsViewModel = hiltViewModel()
                            TransactionsScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}