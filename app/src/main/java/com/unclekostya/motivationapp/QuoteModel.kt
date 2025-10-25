package com.unclekostya.motivationapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteService {
    @GET("random")
    suspend fun getQuote() : List<QuoteResponce>
}

object QuoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val quoteService: QuoteService = retrofit.create(QuoteService::class.java)
}