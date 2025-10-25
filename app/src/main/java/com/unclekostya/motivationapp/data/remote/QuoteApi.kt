package com.unclekostya.motivationapp.data.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object QuoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val quoteService: QuoteService = retrofit.create(QuoteService::class.java)
}