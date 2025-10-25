package com.unclekostya.motivationapp.data.repository

import com.unclekostya.motivationapp.data.remote.QuoteService
import com.unclekostya.motivationapp.domain.model.Quote
import com.unclekostya.motivationapp.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val api: QuoteService
) : QuoteRepository {
    override suspend fun getRandomQuote(): Quote? {
        return try {
            val response = api.getQuote().firstOrNull()
            response?.let {
                Quote(text = it.quoteText, author = it.authorName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
