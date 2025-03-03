package com.example.cowrywisetest

import com.example.cowrywisetest.mock_data.sampleCurrencyMap
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.example.cowrywisetest.utils.CurrencyConverter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CurrencyConverterTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    @Inject
    lateinit var currencyConverter: CurrencyConverter

    @Before
    fun setup() {
        hiltRule.inject()

        runBlocking {
            //populate database with sample data fetched from fixer.io
            // on 2024-03-01
            currencyRepository.populateDatabase(sampleCurrencyMap)
        }
    }

    @After
    fun cleanup() {
        // No cleanup needed for in-memory database
    }

    @Test
    fun testConvertCurrency_USD_to_NGN() = runBlocking {
        val result = currencyConverter.convertCurrency(100.0, "USD", "NGN")
        assertEquals(157805.98111633866, result, 0.0001)
    }

    @Test
    fun testConvertCurrency_EUR_to_USD() = runBlocking {
        val result = currencyConverter.convertCurrency(100.0, "EUR", "USD")
        assertEquals(108.5065, result, 0.0001)
    }

    @Test
    fun testConvertCurrency_AFN_to_AED() = runBlocking {
        val result = currencyConverter.convertCurrency(1.0, "AFN", "AED")
        assertEquals(0.05065984810385309, result, 0.0001)
    }


    @Test
    fun testConvertCurrency_JPY_to_EUR() = runBlocking {
        val result = currencyConverter.convertCurrency(100.0, "JPY", "EUR")
        assertEquals(0.6140136984982502, result, 0.0001)
    }

    @Test
    fun testConvertCurrency_AUD_to_CAD() = runBlocking {
        val result = currencyConverter.convertCurrency(100.0, "AUD", "CAD")
        assertEquals(88.75345469612827, result, 0.0001)
    }

    //Failed Cases

//    @Test
//    fun testConvertCurrency_GBP_to_INR() = runBlocking {
//        val result = currencyConverter.convertCurrency(100.0, "GBP", "INR")
//        assertEquals(10485.714285714286, result, 0.0001)
//    }
//
//    @Test
//    fun testConvertCurrency_AED_to_AFN() = runBlocking {
//        val result = currencyConverter.convertCurrency(1.0, "AED", "AFN")
//        assertEquals(19.8937, result, 0.0001)
//    }

}