package com.unclekostya.motivationapp.domain.usecase

import com.unclekostya.motivationapp.domain.repository.QuoteRepository

class GetRandomQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke() = repository.getRandomQuote()
}