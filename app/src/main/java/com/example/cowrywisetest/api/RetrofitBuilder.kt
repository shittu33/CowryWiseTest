package com.example.cowrywisetest.api

import android.content.Context
import android.system.ErrnoException
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLProtocolException

private const val FIXER_API_KEY = "bc6fa6dfaf9dc776ea75a20482ecb4e8"

object RetrofitBuilder {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://data.fixer.io/api/"

    fun getRetrofit(context: Context): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .protocols(Collections.singletonList(okhttp3.Protocol.HTTP_1_1))
            .hostnameVerifier { _, _ -> true }
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(Interceptor { chain ->
                //if there are open endpoints
                /// add them to the list below
                val openEndpoints = emptyList<String>()
                val isOpen =
                    openEndpoints.isNotEmpty() && openEndpoints.fold(false) { prev, value ->
                        prev || chain.request().url.toString().contains(value)
                    }

                val request: Request =
                    if (!isOpen) {
                        val initialUrl = chain.request().url.toString()
                        val newUrl =
                            if (initialUrl.contains("?"))
                                "$initialUrl&access_key=$FIXER_API_KEY"
                            else
                                "$initialUrl?access_key=$FIXER_API_KEY"

                        chain.request().newBuilder()
                            .url(newUrl)
                            .build()
                    } else {
                        chain.request()
                    }
                chain.proceed(request)
            })
            .build()


        try {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
            }
        } catch (e: SocketTimeoutException) {
            Log.e("RetrofitBuilder", "Socket Timeout Exception", e)
        } catch (e: ConnectException) {
            Log.e("RetrofitBuilder", "Socket Timeout Exception", e)
        } catch (e: UnknownHostException) {
            Log.e("RetrofitBuilder", "Unknown Host Exception", e)
        } catch (e: ProtocolException) {
            Log.e("RetrofitBuilder", "Protocol Exception", e)
        } catch (e: IOException) {
            Log.e("RetrofitBuilder", "IO Exception", e)
        } catch (e: Exception) {
            Log.e("RetrofitBuilder", "Exception", e)
        } catch (e: ErrnoException) {
            Log.e("RetrofitBuilder", "ErrnoException", e)
        } catch (e: SocketException) {
            Log.e("RetrofitBuilder", "Socket Exception", e)
        } catch (e: SocketTimeoutException) {
            Log.e("RetrofitBuilder", "Socket Exception", e)
        } catch (e: EOFException) {
            Log.e("RetrofitBuilder", "EOF Exception", e)
        } catch (e: ProtocolException) {
            Log.e("RetrofitBuilder", "Protocol Exception", e)
        } catch (e: ErrnoException) {
            Log.e("RetrofitBuilder", "ErrnoException", e)
        } catch (e: NoRouteToHostException) {
            Log.e("RetrofitBuilder", "NoRouteToHostException", e)
        } catch (e: SSLProtocolException) {
            Log.e("RetrofitBuilder", "SSLProtocolException", e)
        } catch (e: EOFException) {
            Log.e("RetrofitBuilder", "EOFException", e)
        } catch (e: UnknownHostException) {
            Log.e("RetrofitBuilder", "EOFException", e)
        }

        return retrofit!!
    }

}