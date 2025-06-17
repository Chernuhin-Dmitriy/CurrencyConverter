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
            exchangeRate = exchangeRate
        )
    }

    fun performExchange() {
        val state = _uiState.value
        if (state.fromCurrency.isEmpty() || state.toCurrency.isEmpty()) return

        _uiState.value = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                exchangeCurrencyUseCase(
                    fromCurrency = state.fromCurrency,
                    toCurrency = state.toCurrency,
                    fromAmount = state.fromAmount,
                    toAmount = state.toAmount
                )

                _uiState.value = state.copy(
                    isLoading = false,
                    exchangeCompleted = true
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка при обмене валют"
                )
            }
        }
    }

    fun onExchangeCompletedConsumed() {
        _uiState.value = _uiState.value.copy(exchangeCompleted = false)
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