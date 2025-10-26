package com.unclekostya.motivationapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.unclekostya.motivationapp.data.local.entity.QuoteEntity

@Dao
interface QuoteDao{
    @Query("SELECT * FROM quote")
    fun getAll(): List<QuoteEntity>

    @Insert
    fun insertQuote(quote: QuoteEntity)

    @Delete
    fun removeQuote(quote: QuoteEntity)

    @Query("DELETE FROM quote WHERE uid = :quoteId")
    suspend fun deleteById(quoteId: Int)
}