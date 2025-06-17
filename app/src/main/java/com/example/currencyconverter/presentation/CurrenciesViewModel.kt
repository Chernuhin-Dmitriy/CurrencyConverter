package com.example.currencyconverter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.model.Rate
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.usecase.GetFilteredRatesUseCase
import com.example.currencyconverter.domain.usecase.GetRatesWithBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val getRatesWithBalanceUseCase: GetRatesWithBalanceUseCase,
    private val getFilteredRatesUseCase: GetFilteredRatesUseCase,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrenciesUiState())
    val uiState: StateFlow<CurrenciesUiState> = _uiState.asStateFlow()

    private var ratesUpdateJob: Job? = null

    init {
        initializeAccount()
        startRatesUpdates()
    }

    private fun initializeAccount() {
        viewModelScope.launch {
            accountRepository.initializeDefaultAccount()
        }
    }

    fun selectCurrency(currencyCode: String) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency = currencyCode,
            inputAmount = ""
        )
        startRatesUpdates()
    }

    fun onAmountChanged(amount: String) {
        val numericAmount = amount.toDoubleOrNull()
        _uiState.value = _uiState.value.copy(inputAmount = amount)

        if (numericAmount != null && numericAmount > 0) {
            loadFilteredRates(numericAmount)
        } else {
            startRatesUpdates()
        }
    }

    fun clearAmount() {
        _uiState.value = _uiState.value.copy(inputAmount = "")
        startRatesUpdates()
    }

    fun onCurrencyClick(currencyCode: String) {
        val currentAmount = _uiState.value.inputAmount.toDoubleOrNull()
        if (currentAmount != null && currentAmount > 0) {
            // Переход на экран обмена
            val selectedRate = _uiState.value.rates.find { it.currencyCode == currencyCode }
            selectedRate?.let { rate ->
                _uiState.value = _uiState.value.copy(
                    navigationEvent = NavigationEvent.ToExchange(
                        fromCurrency = _uiState.value.selectedCurrency,
                        toCurrency = currencyCode,
                        fromAmount = currentAmount,
                        toAmount = rate.value,
                        exchangeRate = rate.value / currentAmount
                    )
                )
            }
        } else {
            // Обычное переключение валюты
            selectCurrency(currencyCode)
        }
    }

    fun onNavigationEventConsumed() {
        _uiState.value = _uiState.value.copy(navigationEvent = null)
    }

    private fun startRatesUpdates() {
        ratesUpdateJob?.cancel()
        ratesUpdateJob = viewModelScope.launch {
            while (true) {
                try {
                    val amount = _uiState.value.inputAmount.toDoubleOrNull() ?: 1.0
                    val rates = getRatesWithBalanceUseCase(
                        _uiState.value.selectedCurrency,
                        amount
                    )

                    // Сортируем: выбранная валюта первая, остальные по алфавиту
                    val sortedRates = rates.sortedWith { a, b ->
                        when {
                            a.currencyCode == _uiState.value.selectedCurrency -> -1
                            b.currencyCode == _uiState.value.selectedCurrency -> 1
                            else -> a.currencyCode.compareTo(b.currencyCode)
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        rates = sortedRates,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                delay(1000) // Обновляем каждую секунду согласно ТЗ
            }
        }
    }

    private fun loadFilteredRates(desiredAmount: Double) {
        ratesUpdateJob?.cancel()
        ratesUpdateJob = viewModelScope.launch {
            while (true) {
                try {
                    val rates = getFilteredRatesUseCase(
                        _uiState.value.selectedCurrency,
                        desiredAmount
                    )

                    // Сортируем: выбранная валюта первая, остальные по алфавиту
                    val sortedRates = rates.sortedWith { a, b ->
                        when {
                            a.currencyCode == _uiState.value.selectedCurrency -> -1
                            b.currencyCode == _uiState.value.selectedCurrency -> 1
                            else -> a.currencyCode.compareTo(b.currencyCode)
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        rates = sortedRates,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ratesUpdateJob?.cancel()
    }
}

data class CurrenciesUiState(
    val selectedCurrency: String = "RUB",
    val inputAmount: String = "",
    val rates: List<Rate> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val navigationEvent: NavigationEvent? = null
)

sealed class NavigationEvent {
    data class ToExchange(
        val fromCurrency: String,
        val toCurrency: String,
        val fromAmount: Double,
        val toAmount: Double,
        val exchangeRate: Double
    ) : NavigationEvent()
}