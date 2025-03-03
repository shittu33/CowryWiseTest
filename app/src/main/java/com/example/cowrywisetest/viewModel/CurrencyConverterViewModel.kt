package com.example.cowrywisetest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.example.cowrywisetest.utils.CalendarWrapper
import com.example.cowrywisetest.utils.DateUtil
import com.example.cowrywisetest.utils.Event
import com.example.cowrywisetest.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
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
}