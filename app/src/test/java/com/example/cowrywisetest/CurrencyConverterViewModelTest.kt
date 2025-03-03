package com.example.cowrywisetest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.cowrywisetest.converter.getResult
import com.example.cowrywisetest.mock_data.mockHistoricalRatesResponseBody
import com.example.cowrywisetest.mock_data.mockLatestRatesFullResponseBody
import com.example.cowrywisetest.mock_data.mockSymbolResponseBody
import com.example.cowrywisetest.models.LatestRateResponseModel
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.example.cowrywisetest.utils.CalendarWrapper
import com.example.cowrywisetest.utils.DateUtil
import com.example.cowrywisetest.utils.Event
import com.example.cowrywisetest.utils.NetworkResult
import com.example.cowrywisetest.viewModel.CurrencyConverterViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import retrofit2.Response
import java.util.Calendar

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // For LiveData testing

    private lateinit var viewModel: CurrencyConverterViewModel
    private lateinit var mockCurrencyRepository: CurrencyRepository
    private lateinit var mockGson: Gson
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        // Set up the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Mock dependencies
        mockCurrencyRepository = mock(CurrencyRepository::class.java)
        //Gson
        mockGson = Gson()

        // Create the ViewModel with mocked dependencies
        viewModel = CurrencyConverterViewModel(mockCurrencyRepository)
    }

    @After
    fun cleanup() {
        // Reset the main dispatcher
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testGetCurrencySymbols() = runBlockingTest {
        // 1. Mock the repository response
        val mockResponse =
            Response.success(mockSymbolResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                .getResult(SupportedSymbolsResponseModel::class.java, mockGson)

        `when`(mockCurrencyRepository.getSymbols()).thenReturn(flowOf(mockResponse))

        // 2. Observe the LiveData
        val observer = mock(Observer::class.java) as Observer<Event<NetworkResult<SupportedSymbolsResponseModel>>>
        viewModel.symbolsLiveData.observeForever(observer)

        // 3. Call the method under test
        viewModel.getCurrencySymbols()

        // 4. Verify the LiveData value
        verify(observer).onChanged(Event(NetworkResult.Loading))
        verify(observer).onChanged(Event(mockResponse))
    }

    @Test
    fun testPopulateLatestRatesToDb() = runBlockingTest {
        // 1. Mock the repository response
        val mockResponse = Response.success(mockLatestRatesFullResponseBody .toResponseBody("application/json".toMediaTypeOrNull()))
            .getResult(LatestRateResponseModel::class.java, mockGson)

        `when`(mockCurrencyRepository.getLatestRates()).thenReturn(flowOf(mockResponse))

        // 2. Mock the repository response for populateDatabase
        `when`(mockCurrencyRepository.populateDatabase(any())).thenReturn(flowOf(Unit))

        // 3. Observe the LiveData
        val observer = mock(Observer::class.java) as Observer<Event<NetworkResult<String>>>
        viewModel.populateRatesDbLiveData.observeForever(observer)

        // 3. Call the method under test
        viewModel.populateLatestRatesToDb()

        // 4. Verify the LiveData value
        verify(observer).onChanged(Event(NetworkResult.Loading))
        verify(observer).onChanged(Event(NetworkResult.Success("success")))
    }

    @Test
    fun testGetLastDaysCurrencyData() = runBlockingTest {
        // 1. Mock the repository response
        val mockResponse = Response.success(mockHistoricalRatesResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
            .getResult(LatestRateResponseModel::class.java, mockGson)


        val symbol = "USD"
        val date = "2024-03-01"
        `when`(mockCurrencyRepository.getHistoricalRates(any(),  any())).thenReturn(flowOf(mockResponse))

        // 2. Observe the LiveData
        val progressObserver = mock(Observer::class.java) as Observer<Event<NetworkResult<String>>>
        val ratesObserver = mock(Observer::class.java) as Observer<Map<String, Double>>
        viewModel.historicalProgressLiveData.observeForever(progressObserver)
        viewModel.historicalRatesLiveData.observeForever(ratesObserver)


        // 3. Call the method under test
        viewModel.getLastDaysCurrencyData(symbol=symbol)

        // 4. Verify the LiveData values
        verify(progressObserver).onChanged(Event(NetworkResult.Loading))
        verify(progressObserver).onChanged(Event(NetworkResult.Success("Success")))
    }
}