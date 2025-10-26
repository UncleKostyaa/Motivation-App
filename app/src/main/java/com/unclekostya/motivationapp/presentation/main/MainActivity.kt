package com.unclekostya.motivationapp.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unclekostya.motivationapp.data.remote.QuoteService
import com.unclekostya.motivationapp.data.repository.QuoteRepositoryImpl
import com.unclekostya.motivationapp.domain.usecase.GetRandomQuoteUseCase
import com.unclekostya.motivationapp.presentation.navigation.NavGraph
import com.unclekostya.motivationapp.presentation.quote.QuoteViewModel
import com.unclekostya.motivationapp.ui.theme.MotivationAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unclekostya.motivationapp.R
import com.unclekostya.motivationapp.data.local.AppDatabase

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemDark = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemDark) }

            val api = Retrofit.Builder()
                .baseUrl("https://zenquotes.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuoteService::class.java)

            val repository = QuoteRepositoryImpl(api)
            val viewModel = remember {
                QuoteViewModel(GetRandomQuoteUseCase(repository))
            }


            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "quote"
            ).build()

            MotivationAppTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            title = { Text("Motivation App", style = MaterialTheme.typography.titleLarge) }
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
                ) {  innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        NavGraph(
                            navController = navController,
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = { isDarkTheme = !isDarkTheme },
                            db = db
                        )
                    }
                }
            }
        }
    }
}
