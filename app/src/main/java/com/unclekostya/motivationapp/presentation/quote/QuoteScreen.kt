package com.unclekostya.motivationapp.presentation.quote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unclekostya.motivationapp.data.local.AppDatabase
import com.unclekostya.motivationapp.data.local.entity.QuoteEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun QuoteScreen(
    viewModel: QuoteViewModel,
    db: AppDatabase
) {
    val quote by viewModel.quote.collectAsState()
    val quoteDao = db.quoteDao()
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = quote?.text ?: "Loading...",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = quote?.author ?: "",
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                Button(onClick = { viewModel.loadQuote() }) {
                    Text("Next Quote")
                }

                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            quote?.let {
                                quoteDao.insertQuote(
                                    QuoteEntity(
                                        text = it.text,
                                        authorName = it.author
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(start = 24.dp)
                ) {
                    Text("Add to favourite")
                }
            }
        }
    }
}
