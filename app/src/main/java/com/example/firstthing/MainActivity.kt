package com.example.firstthing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.firstthing.ui.theme.FirstThingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstThingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        onGoToSecond = {
                            startActivity(Intent(this, SecondActivity::class.java))
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onGoToSecond: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val groupMembers = remember {
        listOf("Kristaps Loginovs", "Reinis Sausiņš", "Ričards Ābols")
    }
    val checkedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            groupMembers.forEach { put(it, false) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "1-st Activity",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onGoToSecond) {
            Text("Go to 2-nd")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { showDialog = true }) {
            Text("Dialog")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                Toast.makeText(context, "You closed dialog", Toast.LENGTH_SHORT).show()
                showDialog = false
            },
            title = {
                Text("3-rd Group's Dialog")
            },
            text = {
                Column {
                    groupMembers.forEach { member ->
                        val isChecked = checkedStates[member] ?: false
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .toggleable(
                                    value = isChecked,
                                    role = Role.Checkbox,
                                    onValueChange = { checked ->
                                        checkedStates[member] = checked
                                        val action = if (checked) "checked" else "unchecked"
                                        Toast.makeText(context, "$member $action", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null
                            )
                            Text(
                                text = member,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Toast.makeText(context, "You clicked OK", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        Toast.makeText(context, "You closed dialog", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}
