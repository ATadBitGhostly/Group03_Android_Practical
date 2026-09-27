package com.example.firstthing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.firstthing.ui.theme.FirstThingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstThingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FirstActivityScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FirstActivityScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "1-st Activity")

        Button(onClick = {
            context.startActivity(Intent(context, SecondActivity::class.java))
        }) {
            Text("Go to 2-nd")
        }

        Button(onClick = { showDialog = true }) {
            Text("Dialog")
        }
    }

    if (showDialog) {
        GroupDialog(onDismiss = { showDialog = false })
    }
}

@Composable
fun GroupDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    val members = listOf("Ričards Ābols", "Reinis Sausiņš", "Kristaps Loginovs")
    var checkedStates by remember { mutableStateOf(List(members.size) { false }) }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("3. Group's Dialog") },
        text = {
            Column {
                members.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { isChecked ->
                                checkedStates = checkedStates.toMutableList().also {
                                    it[index] = isChecked
                                }
                                val message = if (isChecked) "$name checked" else "$name unchecked"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        )
                        Text(name)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                Toast.makeText(context, "You clicked OK", Toast.LENGTH_SHORT).show()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                Toast.makeText(context, "You closed dialog", Toast.LENGTH_SHORT).show()
                onDismiss()
            }) {
                Text("Close")
            }
        }
    )
}