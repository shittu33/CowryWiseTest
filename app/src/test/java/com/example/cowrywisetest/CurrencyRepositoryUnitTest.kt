package com.example.cowrywisetest

import com.example.cowrywisetest.api.ApiService
import com.example.cowrywisetest.converter.getResult
import com.example.cowrywisetest.database.dao.EurLatestDao
import com.example.cowrywisetest.database.entity.EurLatestEntity
import com.example.cowrywisetest.mock_data.mockHistoricalRatesResponseBody
import com.example.cowrywisetest.mock_data.mockLatestRatesFullResponseBody
import com.example.cowrywisetest.mock_data.mockSymbolResponseBody
import com.example.cowrywisetest.models.LatestRateResponseModel
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import retrofit2.Response

class CurrencyRepositoryUnitTest {

    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var mockApiService: ApiService
    private lateinit var mockGson: Gson
    private lateinit var mockEurLatestDao: EurLatestDao
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Set up the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Mock dependencies
        mockApiService = mock(ApiService::class.java)
        mockGson = Gson()
        mockEurLatestDao = mock(EurLatestDao::class.java)

        // Create the repository with mocked dependencies
        currencyRepository = CurrencyRepository(mockApiService, mockGson, mockEurLatestDao)
//        currencyRepository = mock(CurrencyRepository::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        // Reset the main dispatcher
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testGetSymbols() = runBlocking {

        // 1. get Mock API response
        val mockResponse =
            Response.success(mockSymbolResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))

        `when`(mockApiService.symbols()).thenReturn(mockResponse)

        // 2. Call the method under test
        val result = currencyRepository.getSymbols().toList()

        // 3. Verify the result

        val expectedResult =
            Response.success(mockSymbolResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                .getResult(SupportedSymbolsResponseModel::class.java, mockGson)

        //compare result from repository with expectedResult
        assertEquals(
            expectedResult, result[0]
        )
    }

    @Test
    fun testGetLatestRates() = runBlocking {
        // get Mock API response
        val mockResponse =
            Response.success(mockLatestRatesFullResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
        `when`(mockApiService.latest()).thenReturn(mockResponse)

        // 2. Call the method under test
        val result = currencyRepository.getLatestRates().toList()

        // 3. Verify the result
        val expectedResult =
            Response.success(mockLatestRatesFullResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                .getResult(LatestRateResponseModel::class.java, mockGson)

        //compare result from repository with expectedResult
        assertEquals(
            expectedResult, result[0]
        )
    }

    @Test
    fun testGetHistoricalRates() = runBlocking {
        //1. get Mock API response
        val date = "2024-03-01"
        val symbols = ""
        val mockResponse =
            Response.success(mockHistoricalRatesResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
        `when`(mockApiService.historicalRates(date,symbols)).thenReturn(mockResponse)

        // 2. Call the method under test
        val result = currencyRepository.getHistoricalRates(date,symbols).toList()

        // 3. Verify the result
        val expectedResult =
            Response.success(mockHistoricalRatesResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                .getResult(LatestRateResponseModel::class.java, mockGson)

        //compare result from repository with expectedResult
        assertEquals(
            expectedResult, result[0]
        )
    }

    @Test
    fun testGetRateBySymbolFromDb() = runBlocking {
        // 1. Mock the DAO response
        val symbol = "USD"
        val rate = 1.037828
        `when`(mockEurLatestDao.getRateBySymbol(symbol)).thenReturn(rate)

        // 2. Call the method under test
        val result = currencyRepository.getRateBySymbolFromDb(symbol)

        // 3. Verify the result
        assertEquals(rate, result)
    }

    @Test
    fun testInsertAll() = runBlocking {
        // 1. Create mock data
        val rates = listOf(
            EurLatestEntity(symbol = "USD", rate = 1.037828),
            EurLatestEntity(symbol = "NGN", rate = 1559.341054)
        )

        // 2. Call the method under test
        currencyRepository.insertAll(rates)

        // 3. Verify that the DAO's insertAll method was called
        verify(mockEurLatestDao).insertAll(rates)
    }

    @Test
    fun testPopulateDatabase() = runBlocking {
        // 1. Create mock data
        val rates = mapOf(
            "USD" to 1.037828,
            "NGN" to 1559.341054
        )

        runBlocking {
            // 2. Call the method under test
            currencyRepository.populateDatabase(rates)
        }

        // 3. Verify that the DAO's insertAll method was called
        val expectedEntities = rates.map { EurLatestEntity(symbol = it.key, rate = it.value) }
        verify(mockEurLatestDao).insertAll(expectedEntities)
    }
}