package com.example.firstthing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstthing.ui.theme.FirstThingTheme
import kotlinx.coroutines.launch

class FourthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val themeSelection by getThemeOption(context).collectAsState(initial = ThemeOption.SYSTEM)

            val isDarkTheme = when (themeSelection) {
                ThemeOption.LIGHT -> false
                ThemeOption.DARK -> true
                ThemeOption.SYSTEM -> isSystemInDarkTheme()
            }

            FirstThingTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        SavingFunction(
                            selectedTheme = themeSelection,
                            onThemeSelected = { newTheme ->
                                scope.launch {
                                    saveThemeOption(context, newTheme)
                                }
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavingFunction(
    selectedTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val savedText by getInputText(context).collectAsState(initial = "")
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(savedText) {
        if (inputText.isEmpty() && savedText.isNotEmpty()) {
            inputText = savedText
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            MinimalDropdownMenu(
                selectedTheme = selectedTheme,
                onThemeOptionSelected = onThemeSelected
            )
        }

        // Center content layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Activity 4, but for homework it is activity 1",
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Write here") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    saveInputText(context, inputText)
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                context.startActivity(Intent(context, FifthActivity::class.java))
            }) {
                Text("Proceed to 2nd Activity!")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(intent)
                (context as? Activity)?.finish()
            }) {
                Text("Go Back")
            }
        }
    }
}

@Composable
fun MinimalDropdownMenu(
    selectedTheme: ThemeOption,
    onThemeOptionSelected: (ThemeOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        if (selectedTheme == ThemeOption.SYSTEM) "✓ Default (System)" else "Default (System)"
                    )
                },
                onClick = {
                    onThemeOptionSelected(ThemeOption.SYSTEM)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        if (selectedTheme == ThemeOption.DARK) "✓ Dark Side" else "Dark Side"
                    )
                },
                onClick = {
                    onThemeOptionSelected(ThemeOption.DARK)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        if (selectedTheme == ThemeOption.LIGHT) "✓ Light Side" else "Light Side"
                    )
                },
                onClick = {
                    onThemeOptionSelected(ThemeOption.LIGHT)
                    expanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstThingTheme {
        SavingFunction(
            selectedTheme = ThemeOption.SYSTEM,
            onThemeSelected = {}
        )
    }
}