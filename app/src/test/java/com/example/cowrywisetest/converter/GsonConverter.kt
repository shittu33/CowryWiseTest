package com.example.cowrywisetest.converter

import com.example.cowrywisetest.models.SupportedSymbolsResponseModel
import com.example.cowrywisetest.utils.NetworkResult
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


// This function is used to get the result from
// the response body, ensure that your response body model
// inherit from BaseResponseBodyModel
fun <T> Response<ResponseBody>.getResult(successType: Class<T>, gson: Gson): NetworkResult<T> {

    return body()?.use {

        val responseString = it.string()

//        println("responseString is $responseString")

        if (responseString.isSuccess(gson)) NetworkResult.Success(
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

fun String.isSuccess(gson: Gson): Boolean {

    val jsonMap = gson.fromJson<Map<Any, Any>>(this, Map::class.java)

    return !jsonMap.isNullOrEmpty() && !jsonMap.contains("error")


}
