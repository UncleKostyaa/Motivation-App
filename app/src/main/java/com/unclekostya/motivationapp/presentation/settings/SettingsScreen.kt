package com.unclekostya.motivationapp.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SettingsPage(
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    var checked by remember { mutableStateOf(isDarkTheme) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(top = 12.dp,)
        )
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Dark mode",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(24.dp)
            )

            Switch(

                checked = checked,
                onCheckedChange = {
                    checked = it
                    onToggleTheme()
                },
                modifier = Modifier
                    .padding(20.dp)
            )
        }
    }
}
