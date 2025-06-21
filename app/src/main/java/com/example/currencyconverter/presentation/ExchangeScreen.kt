package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Toolbar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Text(
                        text = "${uiState.fromCurrency} to ${uiState.toCurrency}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Покупаемая валюта (сверху, зеленая)
                ExchangeCurrencyCard(
                    currencyCode = uiState.toCurrency,
                    amount = uiState.toAmount,
                    context = context,
                    isPositive = true,
                    prefix = "+"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Продаваемая валюта (снизу, красная)
                ExchangeCurrencyCard(
                    currencyCode = uiState.fromCurrency,
                    amount = uiState.fromAmount,
                    context = context,
                    isPositive = false,
                    prefix = "-"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Курс обмена
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Exchange Rate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "€1 = ₽${CurrencyUtils.formatAmount(uiState.exchangeRate)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Сообщение об ошибке
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .height(56.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007AFF)
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Buy Euro for Russia Rouble",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ExchangeCurrencyCard(
    currencyCode: String,
    amount: Double,
    context: android.content.Context,
    isPositive: Boolean,
    prefix: String
) {
    val currencyInfo = CurrencyUtils.getCurrencyInfo(context, currencyCode)
    val backgroundColor = if (isPositive) {
        Color(0xFFE8F5E8) // Светло-зеленый
    } else {
        Color(0xFFFFEBEE) // Светло-красный
    }

    val textColor = if (isPositive) {
        Color(0xFF2E7D32) // Темно-зеленый
    } else {
        Color(0xFFD32F2F) // Темно-красный
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Флаг и информация о валюте
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Флаг валюты
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = CurrencyUtils.getCurrencyFlag(currencyCode),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = currencyCode,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = currencyInfo.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }

            // Сумма
            Text(
                text = "$prefix${CurrencyUtils.formatAmount(amount)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.End
            )
        }
    }
}