package com.example.firstthing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firstthing.ui.theme.FirstThingTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val themeSelection by getThemeOption(context).collectAsState(initial = ThemeOption.SYSTEM)
            val isDarkTheme = when (themeSelection) {
                ThemeOption.LIGHT -> false
                ThemeOption.DARK -> true
                ThemeOption.SYSTEM -> isSystemInDarkTheme()
            }
            FirstThingTheme (darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SecondActivityScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SecondActivityScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Text("2-nd Activity")
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("get text here") }
        )

        Button(onClick = {
            scope.launch {
                val storedText = getInputText(context).first()
                if (storedText.isEmpty()) {
                    Toast.makeText(context, "No saved text", Toast.LENGTH_SHORT).show()
                } else {
                    inputText = storedText
                    Toast.makeText(context, "Read from storage", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Read storage")
        }

        Button(onClick = {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            (context as ComponentActivity).finish()
        }) {
            Text("Back")
        }
    }
}