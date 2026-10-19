package com.example.firstthing

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.firstthing.ui.theme.FirstThingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeOption {
    LIGHT,
    DARK,
    SYSTEM
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val INPUT_TEXT_KEY = stringPreferencesKey("input_text")
val THEME_OPTION_KEY = stringPreferencesKey("theme_option")

// 2. Save function (suspending)
suspend fun saveInputText(context: Context, text: String) {
    context.dataStore.edit { preferences ->
        preferences[INPUT_TEXT_KEY] = text
    }
}

// 3. Read Flow
fun getInputText(context: Context): Flow<String> {
    return context.dataStore.data.map { preferences ->
        preferences[INPUT_TEXT_KEY] ?: ""
    }
}

// Save theme selection to DataStore
suspend fun saveThemeOption(context: Context, themeOption: ThemeOption) {
    context.dataStore.edit { preferences ->
        preferences[THEME_OPTION_KEY] = themeOption.name
    }
}

// Read theme selection Flow from DataStore
fun getThemeOption(context: Context): Flow<ThemeOption> {
    return context.dataStore.data.map { preferences ->
        val name = preferences[THEME_OPTION_KEY] ?: ThemeOption.SYSTEM.name
        try {
            ThemeOption.valueOf(name)
        } catch (e: Exception) {
            ThemeOption.SYSTEM
        }
    }
}

class InputSave : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstThingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Text(
                            text = "DataStore Preferences Storage",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}