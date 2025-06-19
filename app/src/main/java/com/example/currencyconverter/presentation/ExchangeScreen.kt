package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyconverter.R
import com.example.currencyconverter.utils.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    fromCurrency: String,
    toCurrency: String,
    fromAmount: Double,
    toAmount: Double,
    exchangeRate: Double,
    onNavigateBack: () -> Unit,
    viewModel: ExchangeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Инициализация данных обмена
    LaunchedEffect(fromCurrency, toCurrency, fromAmount, toAmount, exchangeRate) {
        viewModel.setExchangeData(fromCurrency, toCurrency, fromAmount, toAmount, exchangeRate)
    }

    // Обработка завершения обмена
    LaunchedEffect(uiState.exchangeCompleted) {
        if (uiState.exchangeCompleted) {
            viewModel.onExchangeCompletedConsumed()
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null //stringResource(R.string.back)
                )
            }

            Text(
                text = stringResource(R.string.nav_exchange),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Карточка с информацией об обмене
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // От валюты
                ExchangeCurrencyInfo(
                    currencyCode = uiState.fromCurrency,
                    amount = uiState.fromAmount,
                    context = context,
                    isFrom = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Стрелка
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // К валюте
                ExchangeCurrencyInfo(
                    currencyCode = uiState.toCurrency,
                    amount = uiState.toAmount,
                    context = context,
                    isFrom = false
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Курс обмена
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.exchange_rate),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "1 ${uiState.fromCurrency} = ${CurrencyUtils.formatAmount(uiState.exchangeRate)} ${uiState.toCurrency}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Сообщение об ошибке
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Кнопка покупки
        Button(
            onClick = { viewModel.performExchange() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = stringResource(R.string.buy_button),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ExchangeCurrencyInfo(
    currencyCode: String,
    amount: Double,
    context: android.content.Context,
    isFrom: Boolean
) {
    val currencyInfo = CurrencyUtils.getCurrencyInfo(context, currencyCode)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Флаг валюты (заглушка)
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isFrom) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currencyCode.take(2),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isFrom) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currencyInfo.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Text(
            text = currencyCode,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = CurrencyUtils.formatAmount(amount),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = if (isFrom) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}