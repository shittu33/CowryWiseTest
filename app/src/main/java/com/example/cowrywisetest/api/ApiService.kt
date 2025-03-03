package com.example.cowrywisetest.api

import com.example.cowrywisetest.models.LatestRateResponseModel
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("symbols")
    suspend fun symbols(
    ): Response<ResponseBody>

    @GET("latest")
    suspend fun latest(
    ): Response<ResponseBody>

    @GET("latest")
    suspend fun latestWithSymbols(
        //comma separated strings
        @Query("symbols") symbols:String
    ): Response<ResponseBody>

    @GET("{date}")
    suspend fun historicalRates(
        //e.g 2024-03-01
        @Path("date") date: String,
        @Query("symbols") symbols:String
    ): Response<ResponseBody>

}