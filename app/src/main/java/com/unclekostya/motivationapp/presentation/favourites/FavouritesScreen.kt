package com.unclekostya.motivationapp.presentation.favourites

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unclekostya.motivationapp.data.local.AppDatabase
import com.unclekostya.motivationapp.data.local.entity.QuoteEntity
import com.unclekostya.motivationapp.R
import com.unclekostya.motivationapp.data.local.dao.QuoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FavouritesScreen(db: AppDatabase) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val scope = rememberCoroutineScope()
        val quoteDao = db.quoteDao()
        var favouriteQuotes by remember { mutableStateOf(emptyList<QuoteEntity>()) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                val quotes = quoteDao.getAll()
                withContext(Dispatchers.Main) {
                    favouriteQuotes = quotes
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            favouriteQuotes.forEach { quote ->
                DrawQuoteCard(
                    quote = quote,
                    onDelete = {
                        scope.launch(Dispatchers.IO) {
                            quoteDao.deleteById(quote.uid)
                            favouriteQuotes = quoteDao.getAll()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DrawQuoteCard(
    quote: QuoteEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = quote.text ?: "",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = quote.authorName ?: "",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic
            )
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 36.dp)
            ) {
                Icon(
                   painter = painterResource(R.drawable.delete_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                   contentDescription = "delete",
                   modifier = Modifier
                       .size(72.dp)
                )
            }
        }
    }
}