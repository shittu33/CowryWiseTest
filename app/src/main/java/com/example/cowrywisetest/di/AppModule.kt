package com.example.cowrywisetest.di

import android.content.Context
import androidx.room.Room
import com.example.cowrywisetest.api.ApiService
import com.example.cowrywisetest.api.RetrofitBuilder
import com.example.cowrywisetest.database.AppDatabase
import com.example.cowrywisetest.database.dao.EurLatestDao
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(@ApplicationContext context: Context) =
        RetrofitBuilder.getRetrofit(context).create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideGson() = Gson()


    @Provides
    @Singleton
    fun provideCurrencyRepository(
        apiService: ApiService,
        gson: Gson,
        eurLatestDao: EurLatestDao
    ): CurrencyRepository {
        return CurrencyRepository(
            apiService, gson, eurLatestDao
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEurLatestDao(database: AppDatabase): EurLatestDao {
        return database.eurLatestDao()
    }
}