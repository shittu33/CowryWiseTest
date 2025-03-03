package com.example.cowrywisetest.utils

import com.example.cowrywisetest.repositories.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        return (amount * (destRate / baseRate))
    }

    fun convertCurrencyFlow(
        amount: Double,
        baseSymbol: String,
        destSymbol: String
    ): Flow<Double> {
        return flow {

            emit(convertCurrency(amount, baseSymbol, destSymbol))
        }.flowOn(Dispatchers.IO)
    }
}