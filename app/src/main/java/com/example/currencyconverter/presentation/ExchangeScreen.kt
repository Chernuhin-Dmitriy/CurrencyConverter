package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyconverter.utils.CurrencyUtils

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

                Spacer(modifier = Modifier.height(16.dp))

                // Курс обмена
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Exchange Rate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Показываем правильный курс: 1 единица получаемой валюты = X единиц платежной валюты
                    val fromInfo = CurrencyUtils.getCurrencyInfo(context, uiState.fromCurrency)
                    val toInfo = CurrencyUtils.getCurrencyInfo(context, uiState.toCurrency)

                    Text(
                        text = "${toInfo.symbol}1 = ${fromInfo.symbol}${CurrencyUtils.formatAmount(uiState.exchangeRate)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Получаемая валюта (сверху, зеленая) - то, что хотим купить
                ExchangeCurrencyCard(
                    currencyCode = uiState.toCurrency,
                    amount = uiState.toAmount,
                    context = context,
                    isPositive = true,
                    prefix = "+"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Продаваемая валюта (снизу, красная) - то, чем платим
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

        // Ошибка
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

        // Кнопка покупки с динамическим текстом
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
                // Динамический текст кнопки
                val toCurrencyInfo = CurrencyUtils.getCurrencyInfo(context, uiState.toCurrency)
                val fromCurrencyInfo = CurrencyUtils.getCurrencyInfo(context, uiState.fromCurrency)

                Text(
                    text = "Buy ${toCurrencyInfo.name} for ${fromCurrencyInfo.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
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
                FlagImage(
                    currencyCode = currencyCode,
                    size = 70.dp
                )

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
                text = "$prefix${CurrencyUtils.formatAmount(amount)} ${currencyInfo.symbol}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.End
            )
        }
    }
}