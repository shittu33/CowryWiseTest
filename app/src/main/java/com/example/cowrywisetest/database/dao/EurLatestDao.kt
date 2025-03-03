package com.example.cowrywisetest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cowrywisetest.database.entity.EurLatestEntity

@Dao
interface EurLatestDao {
    @Insert
    suspend fun insertAll(rates: List<EurLatestEntity>)

    @Query("SELECT rate FROM eur_latest WHERE symbol = :symbol")
    suspend fun getRateBySymbol(symbol: String): Double?
}