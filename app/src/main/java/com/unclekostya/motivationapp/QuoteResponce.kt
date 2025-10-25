package com.unclekostya.motivationapp

import android.os.Debug
import com.google.gson.annotations.SerializedName

data class QuoteResponce (
@SerializedName("q") val quoteText: String,
@SerializedName("a") val authorName: String,
@SerializedName("h") val preformattedQuote: String

)