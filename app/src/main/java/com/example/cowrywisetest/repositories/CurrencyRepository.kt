package com.example.cowrywisetest.repositories

import com.example.cowrywisetest.api.ApiService
import com.example.cowrywisetest.database.dao.EurLatestDao
import com.example.cowrywisetest.database.entity.EurLatestEntity
import com.example.cowrywisetest.models.LatestRateResponseModel
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.utils.NetworkResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val apiService: ApiService,
    gson: Gson,
    private val eurLatestDao: EurLatestDao
) : BaseRepository(gson) {

    suspend fun getSymbols(): Flow<NetworkResult<SupportedSymbolsResponseModel>> {
        return flow {
            val result = checkNetworkAndStartRequest {
                val response =
                    apiService.symbols()
                response.getResult(SupportedSymbolsResponseModel::class.java)
            }
            emit(result)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLatestRates(): Flow<NetworkResult<LatestRateResponseModel>> {
        return flow {
            val result = checkNetworkAndStartRequest {
                val response =
                    apiService.latest()
                response.getResult(LatestRateResponseModel::class.java)
            }
            emit(result)

        }.flowOn(Dispatchers.IO)
    }

    fun getHistoricalRates(
        date: String, //e.g 2024-03-01
        symbols: String?=null,
    ): Flow<NetworkResult<LatestRateResponseModel>> {
        return flow {
            val result = checkNetworkAndStartRequest {
                val response =
                    apiService.historicalRates(date, symbols ?: "")
                response.getResult(LatestRateResponseModel::class.java)

            }
            emit(result)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRateBySymbolFromDb(symbol: String): Double? {
        return eurLatestDao.getRateBySymbol(symbol)
    }

    suspend fun insertAll(rates: List<EurLatestEntity>) {
        eurLatestDao.insertAll(rates)
    }

    fun populateDatabase(rates: Map<String, Double>): Flow<Unit> {

        val eurLatestList = rates.map { EurLatestEntity(symbol = it.key, rate = it.value) }
        return flow {
            eurLatestDao.insertAll(eurLatestList)
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }

}

