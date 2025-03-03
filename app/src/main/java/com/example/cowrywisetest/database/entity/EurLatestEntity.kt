package com.example.cowrywisetest.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eur_latest")
data class EurLatestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String,
    val rate: Double
)