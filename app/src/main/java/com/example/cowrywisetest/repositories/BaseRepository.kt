package com.example.cowrywisetest.repositories

import android.app.Activity
import android.util.Log
import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.utils.NetworkResult
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository(val gson: Gson) {

    suspend fun <O, T : NetworkResult<O>> checkNetworkAndStartRequest(
        action: suspend () -> T
    ): T {

        try {
            if (!hasInternetConnection()) {
                return NetworkResult.Error.NetworkError() as T
            }
        } catch (e: Exception) {
            e.printStackTrace();NetworkResult.Error.ApiError(message = "Cannot connect to Internet!") as T
        }
        return try {
            action.invoke()
        } catch (e: UnknownHostException) {
            e.printStackTrace();NetworkResult.Error.TimeoutError() as T
        } catch (e: SocketTimeoutException) {
            e.printStackTrace();NetworkResult.Error.TimeoutError() as T
        } catch (e: Exception) {
            e.printStackTrace();NetworkResult.Error.ApiError(message = e.message) as T
        }
    }

    // This function is used to get the result from
    // the response body, ensure that your response body model
    // inherit from BaseResponseBodyModel
    fun <T> Response<ResponseBody>.getResult(successType: Class<T>): NetworkResult<T> {

        return body()?.use {

            val responseString = it.string()

//            println("responseString is $responseString")

            if (responseString.isSuccess) NetworkResult.Success(
                gson.fromJson(
                    responseString,
                    successType
                )
            )
            else {
                val jsonMap = gson.fromJson<Map<Any, Any>>(responseString, Map::class.java)
                if (jsonMap != null)
                    NetworkResult.Error.ApiError(
                        message = jsonMap.getOrDefault("info", "").toString(),
                        error = jsonMap
                    )
                else
                    NetworkResult.Error.ApiError(
                        message = "Something went wrong"
                    )
            }

        } ?: (errorBody().use {

            if (it == null) {
                NetworkResult.Error.NoDataError(code = code())
            } else {
                val stringMap = it.string()
                NetworkResult.Error.ApiError(
                    message = "Something went wrong",
                    error = gson.fromJson<Map<Any, Any>>(
                        stringMap,
                        Map::class.java
                    ),
                    code = code()
                )
            }
        })

    }


    private fun hasInternetConnection(): Boolean {
        return try {
            // Try to connect to a known server (e.g., Google's public DNS)
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            // If the connection was successful, return true
            true
        } catch (e: IOException) {
            // If an exception occurred, return false
            false
        }
    }

    /**
     * A replacement for the default response.isSuccessful since body status codes are used
     * instead of header status codes for determining the status of a request
     * Sigh.
     */
    private val String.isSuccess: Boolean
        get() {

            val jsonMap = gson.fromJson<Map<Any, Any>>(this, Map::class.java)

            return !jsonMap.isNullOrEmpty() && !jsonMap.contains("error")
        }


}
