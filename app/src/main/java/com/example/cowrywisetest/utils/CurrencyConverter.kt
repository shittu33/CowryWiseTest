package com.example.cowrywisetest.utils

import com.example.cowrywisetest.repositories.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyConverter @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun convertCurrency(
        amount: Double,
        baseSymbol: String,
        destSymbol: String
    ): Double {
        // Fetch rates from the repository
        val baseRate = currencyRepository.getRateBySymbolFromDb(baseSymbol)
            ?: throw IllegalArgumentException("Base currency $baseSymbol not found in rates.")
        val destRate = currencyRepository.getRateBySymbolFromDb(destSymbol)
            ?: throw IllegalArgumentException("Destination currency $destSymbol not found in rates.")

        // Perform the conversion
        return amount * (destRate / baseRate)
    }
}