package com.example.currencyconverter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.usecase.ExchangeCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeUiState())
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()

    fun setExchangeData(
        fromCurrency: String,
        toCurrency: String,
        fromAmount: Double,
        toAmount: Double,
        exchangeRate: Double
    ) {
        _uiState.value = _uiState.value.copy(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            fromAmount = fromAmount,
            toAmount = toAmount,
            exchangeRate = exchangeRate,
            errorMessage = null
        )
    }

    fun performExchange() {
        val state = _uiState.value

        // Валидация данных
        if (state.fromCurrency.isEmpty() || state.toCurrency.isEmpty()) {
            _uiState.value = state.copy(
                errorMessage = "Некорректные данные для обмена"
            )
            return
        }

        if (state.fromAmount <= 0 || state.toAmount <= 0) {
            _uiState.value = state.copy(
                errorMessage = "Сумма обмена должна быть больше нуля"
            )
            return
        }

        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                // Выполняем обмен валют
                // fromAmount - сколько списываем (продаем)
                // toAmount - сколько получаем (покупаем)
                exchangeCurrencyUseCase(
                    fromCurrency = state.fromCurrency,
                    toCurrency = state.toCurrency,
                    fromAmount = state.fromAmount,
                    toAmount = state.toAmount
                )

                _uiState.value = state.copy(
                    isLoading = false,
                    exchangeCompleted = true,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка при обмене валют",
                    exchangeCompleted = false
                )
            }
        }
    }

    fun onExchangeCompletedConsumed() {
        _uiState.value = _uiState.value.copy(
            exchangeCompleted = false,
            errorMessage = null
        )
    }
}

data class ExchangeUiState(
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val fromAmount: Double = 0.0,
    val toAmount: Double = 0.0,
    val exchangeRate: Double = 0.0,
    val isLoading: Boolean = false,
    val exchangeCompleted: Boolean = false,
    val errorMessage: String? = null
)