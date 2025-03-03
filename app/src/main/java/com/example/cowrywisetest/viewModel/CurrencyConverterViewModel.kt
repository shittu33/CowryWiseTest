package com.example.cowrywisetest.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowrywisetest.DEFAULT_BASE_CURRENCY
import com.example.cowrywisetest.DEFAULT_TARGET_CURRENCY
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.example.cowrywisetest.utils.CalendarWrapper
import com.example.cowrywisetest.utils.CurrencyConverter
import com.example.cowrywisetest.utils.DateUtil
import com.example.cowrywisetest.utils.Event
import com.example.cowrywisetest.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _symbolsLiveData =
        MutableLiveData<Event<NetworkResult<SupportedSymbolsResponseModel>>>()
    val symbolsLiveData
        get() = _symbolsLiveData

    private val _populateRatesDbLiveData =
        MutableLiveData<Event<NetworkResult<String>>>()
    val populateRatesDbLiveData
        get() = _populateRatesDbLiveData


    private val _historicalRatesLiveData =
        MutableLiveData<Map<String, Double>>()
    val historicalRatesLiveData
        get() = _historicalRatesLiveData


    private val _historicalProgressLiveData =
        MutableLiveData<Event<NetworkResult<String>>>()
    val historicalProgressLiveData
        get() = _historicalProgressLiveData


    private val _conversionResultFlow =
        MutableStateFlow<Double?>(null)
    val conversionResultFlow
        get() = _conversionResultFlow


    private var _baseCurrencySymbol = MutableStateFlow(DEFAULT_BASE_CURRENCY)
    val baseCurrencySymbol
        get() = _baseCurrencySymbol

    private var _targetCurrencySymbol = MutableStateFlow(DEFAULT_TARGET_CURRENCY)
    val targetCurrencySymbol
        get() = _targetCurrencySymbol

    fun setBaseCurrencySymbol(newSymbol:String){
        _baseCurrencySymbol.value = newSymbol
    }

    fun setTargetCurrencySymbol(newSymbol:String){
        _targetCurrencySymbol.value = newSymbol
    }





    suspend fun getCurrencySymbols() {
        currencyRepository.getSymbols().onStart {
            _symbolsLiveData.postValue(Event(NetworkResult.Loading))
        }.collect {
            _symbolsLiveData.postValue(Event(it))
        }
    }

    suspend fun populateLatestRatesToDb() {
        currencyRepository.getLatestRates().onStart {
            _populateRatesDbLiveData.postValue(Event(NetworkResult.Loading))
        }.collect {
            if (it is NetworkResult.Success) {
                currencyRepository.populateDatabase(it.data.rates).collect {
                    _populateRatesDbLiveData.postValue(Event(NetworkResult.Success("success")))
                }
            } else if (it is NetworkResult.Error) {
                _populateRatesDbLiveData.postValue(Event(it))
            }
        }
    }

    suspend fun getLastDaysCurrencyData(
        calendarWrapper: CalendarWrapper = CalendarWrapper(),
        symbol: String
    ) {
        _historicalProgressLiveData.postValue(Event(NetworkResult.Loading))
        val noOfDayBack = 10
        DateUtil.getBackDateRange(calendarWrapper, noOfDayBack).mapIndexed { index, date ->
            currencyRepository.getHistoricalRates(date, symbol).collect {
                if (it is NetworkResult.Success) {
                    if (index == 0) {
                        _historicalRatesLiveData.value = emptyMap()
                    }
                    val rate = it.data.rates[symbol]
                    if (rate != null) {
                        _historicalRatesLiveData.value?.toMutableMap()?.set(date, rate)
                        _historicalRatesLiveData.postValue(_historicalRatesLiveData.value)
                    }
                    if (index == noOfDayBack - 1) {
                        _historicalProgressLiveData.postValue(Event(NetworkResult.Success("Success")))
                    }
                } else {
                    _historicalProgressLiveData.postValue(Event(NetworkResult.Error.ApiError(message = it.toString())))
                }
            }
        }
    }

    fun convertCurrency(
        amount: Double
    ) {
        viewModelScope.launch {
            CurrencyConverter(currencyRepository).convertCurrencyFlow(
                amount,
                _baseCurrencySymbol.value,
                _targetCurrencySymbol.value
            ).collect {
                _conversionResultFlow.value = it
            }
        }
    }
}