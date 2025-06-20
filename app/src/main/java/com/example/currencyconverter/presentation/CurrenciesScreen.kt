package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.model.Rate
import com.example.currencyconverter.utils.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesScreen(
    onNavigateToExchange: (String, String, Double, Double, Double) -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: CurrenciesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Обработка навигационных событий
    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            is NavigationEvent.ToExchange -> {
                onNavigateToExchange(
                    event.fromCurrency,
                    event.toCurrency,
                    event.fromAmount,
                    event.toAmount,
                    event.exchangeRate
                )
                viewModel.onNavigationEventConsumed()
            }
            null -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header с кнопкой транзакций
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.nav_currencies),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onNavigateToTransactions) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = stringResource(R.string.nav_transactions)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Контент
        when {
            uiState.isLoading && uiState.rates.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.rates) { rate ->
                        CurrencyItem(
                            rate = rate,
                            isSelected = rate.currencyCode == uiState.selectedCurrency,
                            isInputMode = uiState.isInputMode && rate.currencyCode == uiState.selectedCurrency,
                            inputAmount = if (rate.currencyCode == uiState.selectedCurrency) uiState.inputAmount else "",
                            onCurrencyClick = { viewModel.onCurrencyClick(rate.currencyCode) },
                            onAmountChanged = { viewModel.onAmountChanged(it) },
                            onClearAmount = { viewModel.clearAmount() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyItem(
    rate: Rate,
    isSelected: Boolean,
    isInputMode: Boolean,
    inputAmount: String,
    onCurrencyClick: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onClearAmount: () -> Unit
) {
    val context = LocalContext.current
    val currencyInfo = CurrencyUtils.getCurrencyInfo(context, rate.currencyCode)
    val focusRequester = remember { FocusRequester() }

    // Автофокус на поле ввода при переходе в режим ввода
    LaunchedEffect(isInputMode) {
        if (isInputMode) {
            focusRequester.requestFocus()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCurrencyClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Флаг валюты (заглушка)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = CurrencyUtils.getCurrencyFlag(rate.currencyCode),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Информация о валюте
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = currencyInfo.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = rate.currencyCode,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Показываем баланс если есть
                rate.accountBalance?.let { balance ->
                    if (balance > 0) {
                        Text(
                            text = stringResource(
                                R.string.balance,
                                CurrencyUtils.formatAmount(balance)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Сумма и ввод
            if (isInputMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputAmount,
                        onValueChange = onAmountChanged,
                        modifier = Modifier
                            .width(120.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.End
                        ),
                        placeholder = {
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.End
                            )
                        }
                    )

                    IconButton(onClick = onClearAmount) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_amount)
                        )
                    }
                }
            } else {
                // Обычное отображение суммы
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = CurrencyUtils.formatAmount(rate.value),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = currencyInfo.symbol,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}