package com.unclekostya.motivationapp.data.remote

import retrofit2.http.GET

interface QuoteService {
    @GET("random")
    suspend fun getQuote(): List<QuoteResponce>
}