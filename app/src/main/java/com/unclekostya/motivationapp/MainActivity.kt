package com.unclekostya.motivationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotivationAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text("Motivation app", fontWeight = FontWeight.Bold)
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(
                                    onClick = {
                                        if (currentRoute != "home") {
                                            navController.navigate("home") {
                                                popUpTo(navController.graph.startDestinationId) {saveState = true}
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(start = 60.dp),
                                    enabled = currentRoute != "home"
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                        contentDescription = "home",
                                        modifier = Modifier
                                            .size(48.dp)

                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if(currentRoute != "favourites") {
                                            navController.navigate("favourites") {
                                                popUpTo(navController.graph.startDestinationId) {saveState = true}
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(start = 60.dp),
                                    enabled =  currentRoute != "favourites"
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.star_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                        contentDescription = "favourites",
                                        modifier = Modifier
                                            .size(48.dp)

                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if(currentRoute != "settings") {
                                            navController.navigate("settings") {
                                                popUpTo(navController.graph.startDestinationId) {saveState = true}
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(start = 60.dp),
                                    enabled = currentRoute != "settings"
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.settings_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                        contentDescription = "settings",
                                        modifier = Modifier
                                            .size(48.dp)

                                    )
                                }
                            }

                        )
                    }

                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        LaunchedEffect(Unit) {
                            viewModel.loadQuote()
                        }
                        NavCont(viewModel = viewModel, navHostController = navController)
                    }
                }
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
fun NavCont(viewModel: QuoteViewModel,navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home") {
            QuoteApp(viewModel = viewModel)
        }
        composable("settings") {
            SettingsPage()
        }
        composable("favourites") {
            FavouritesPage()
        }
    }
}

@Composable
fun QuoteApp(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel
){
    val quote by viewModel.quote.observeAsState()


    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
           colors = CardDefaults.cardColors(
               containerColor = MaterialTheme.colorScheme.surfaceVariant,
           ),
            modifier = modifier
                .size(height = 230.dp, width = 360.dp)
        ) {
            quote?.let {
                Text(
                    text = it.quoteText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = modifier
                        .padding(14.dp)
                )
                Text(
                    text = it.authorName,
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier
                    .padding(16.dp))
            } ?: Text("Loading...")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(top = 12.dp)
        ) {
            Button(onClick = {
                viewModel.loadQuote()
            }) {
                Text("Next quote")
            }
            Button(
                onClick = {
                    ///////////////////////////
                },
                modifier = modifier
                    .padding(start = 10.dp)
            ) {
                Text("To favourites!")
            }
        }
    }
}

@Composable
fun SettingsPage() {
    Text("Settings")
}

@Composable
fun FavouritesPage() {
    Text("Favourites")
}