package com.example.cowrywisetest.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.cowrywisetest.database.dao.EurLatestDao
import com.example.cowrywisetest.database.entity.EurLatestEntity
import kotlinx.coroutines.runBlocking

@Database(entities = [EurLatestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eurLatestDao(): EurLatestDao
}