package com.example.cowrywisetest.module

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.cowrywisetest.api.ApiService
import com.example.cowrywisetest.api.RetrofitBuilder
import com.example.cowrywisetest.database.AppDatabase
import com.example.cowrywisetest.database.dao.EurLatestDao
import com.example.cowrywisetest.di.AppModule
import com.example.cowrywisetest.repositories.CurrencyRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Module
object TestDatabaseModule {


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
    fun provideAppDatabase(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun provideEurLatestDao(database: AppDatabase): EurLatestDao {
        return database.eurLatestDao()
    }
}
