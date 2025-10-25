package com.unclekostya.motivationapp.domain.repository

import com.unclekostya.motivationapp.domain.model.Quote

interface QuoteRepository {
    suspend fun getRandomQuote(): Quote?
}