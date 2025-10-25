package com.unclekostya.motivationapp.data.remote

import com.google.gson.annotations.SerializedName

data class QuoteResponce (
    @SerializedName("q") val quoteText: String,
    @SerializedName("a") val authorName: String,
    @SerializedName("h") val preformattedQuote: String

)