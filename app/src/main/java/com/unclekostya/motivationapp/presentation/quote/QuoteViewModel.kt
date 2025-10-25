package com.unclekostya.motivationapp.presentation.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unclekostya.motivationapp.domain.model.Quote
import com.unclekostya.motivationapp.domain.usecase.GetRandomQuoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase
) : ViewModel() {

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote
    init {
        loadQuote()
    }

    fun loadQuote() {
        viewModelScope.launch {
            _quote.value = getRandomQuoteUseCase()
        }
    }
}
