package com.unclekostya.motivationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.unclekostya.motivationapp.ui.theme.MotivationAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: QuoteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
                    val repository = QuoteRepository(QuoteApi.quoteService)
                    @Suppress("UNCHECKED_CAST")
                    return QuoteViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotivationAppTheme {
                QuoteApp(viewModel=viewModel)
            }
        }
    }
}


class QuoteRepository(private val api: QuoteService) {
    suspend fun getQuote(): QuoteResponce? {
        return try {
            val response = api.getQuote()
            response.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    private val _quote = MutableLiveData<QuoteResponce?>()
    val quote: LiveData<QuoteResponce?> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            _quote.value = repository.getQuote()
        }
    }

}

@Composable
fun QuoteApp(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel
){
    val quote by viewModel.quote.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuote()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        quote?.let {
            Text(text = it.quoteText)
            Text(text = it.authorName)
        } ?: Text("Загрузка цитаты...")

    }
}