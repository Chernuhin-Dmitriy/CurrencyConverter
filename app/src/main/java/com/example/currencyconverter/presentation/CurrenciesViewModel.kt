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
            try {
                accountRepository.initializeDefaultAccount()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка инициализации: ${e.message}"
                )
            }
        }
    }

    fun selectCurrency(currencyCode: String) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency = currencyCode,
            inputAmount = "",
            isInputMode = false,
            errorMessage = null // Очищаем ошибку при смене валюты
        )
        startRatesUpdates()
    }

    fun onAmountChanged(amount: String) {
        _uiState.value = _uiState.value.copy(
            inputAmount = amount,
            isInputMode = true,
            errorMessage = null // Очищаем ошибку при вводе
        )

        // Добавляем небольшую задержку для debounce
        ratesUpdateJob?.cancel()
        ratesUpdateJob = viewModelScope.launch {
            delay(300) // Задержка 300мс для debounce

            val numericAmount = amount.toDoubleOrNull()
            if (numericAmount != null && numericAmount > 0) {
                loadFilteredRates(numericAmount)
            } else if (amount.isEmpty()) {
                // Если поле пустое, показываем обычные курсы
                startRatesUpdates()
            }
            // Если введено некорректное число, просто оставляем текущий список
        }
    }

    fun clearAmount() {
        _uiState.value = _uiState.value.copy(
            inputAmount = "",
            isInputMode = false,
            errorMessage = null
        )
        startRatesUpdates()
    }

    fun onCurrencyClick(currencyCode: String) {
        val currentState = _uiState.value
        val currentAmount = currentState.inputAmount.toDoubleOrNull()

        if (currentState.isInputMode && currentAmount != null && currentAmount > 0) {
            // Переход на экран обмена
            val selectedRate = currentState.rates.find { it.currencyCode == currencyCode }
            selectedRate?.let { rate ->
                _uiState.value = currentState.copy(
                    navigationEvent = NavigationEvent.ToExchange(
                        fromCurrency = currentState.selectedCurrency,
                        toCurrency = currencyCode,
                        fromAmount = currentAmount,
                        toAmount = rate.value,
                        exchangeRate = rate.value / currentAmount
                    )
                )
            }
        } else if (currencyCode == currentState.selectedCurrency && !currentState.isInputMode) {
            // Клик по уже выбранной валюте - переходим в режим ввода
            _uiState.value = currentState.copy(
                inputAmount = "1",
                isInputMode = true,
                errorMessage = null
            )
        } else {
            // Обычное переключение валюты
            selectCurrency(currencyCode)
        }
    }

    fun onNavigationEventConsumed() {
        _uiState.value = _uiState.value.copy(navigationEvent = null)
    }

    private fun startRatesUpdates() {
        cancelCurrentJob()
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
                        isLoading = false,
                        errorMessage = null
                    )
                } catch (e: Exception) {
                    // Проверяем, что корутина не была отменена
                    if (e !is kotlinx.coroutines.CancellationException) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Ошибка загрузки курсов: ${e.message}"
                        )
                    }
                }
                delay(1000) // Обновляем каждую секунду согласно ТЗ
            }
        }
    }

    private fun loadFilteredRates(desiredAmount: Double) {
        cancelCurrentJob()
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
                        isLoading = false,
                        errorMessage = null
                    )
                } catch (e: Exception) {
                    // Проверяем, что корутина не была отменена
                    if (e !is kotlinx.coroutines.CancellationException) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Ошибка загрузки курсов: ${e.message}"
                        )
                    }
                }
                delay(1000)
            }
        }
    }

    private fun cancelCurrentJob() {
        ratesUpdateJob?.cancel()
        ratesUpdateJob = null
    }

    override fun onCleared() {
        super.onCleared()
        cancelCurrentJob()
    }
}

data class CurrenciesUiState(
    val selectedCurrency: String = "RUB",
    val inputAmount: String = "",
    val isInputMode: Boolean = false,
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