package com.unclekostya.motivationapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unclekostya.motivationapp.data.local.dao.QuoteDao
import com.unclekostya.motivationapp.data.local.entity.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao
}