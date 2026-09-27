package com.example.firstthing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstthing.ui.theme.FirstThingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstThingTheme {
                Surface {
                    FirstActivityScreen()
                }
            }
        }
    }
}

@Composable
fun FirstActivityScreen() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "1st Activity")
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            context.startActivity(Intent(context, SecondActivity::class.java))
        }){
            Text(
                text = "Go to 2nd Activity",
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showDialog = true }) {
            Text("Dialog")
        }
    }

    if (showDialog) {
        GroupDialog(onDismiss = { showDialog = false })
    }
//    Column(modifier = Modifier) {
//        Text(
//            text = "This is the first application from Group 3:\nReinis Sausiņš\nRičards Ābols\nKristaps Loginovs\nAnd the application was developed by 'Reinis Sausiņš'",
//            modifier = Modifier
//        )
//    }
}

@Composable
fun GroupDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    // Replace these with your actual group members' names
    val members = listOf(
        "Reinis Sausiņš 1",
        "Ričards Ābols 2",
        "Kristaps Loginovs 3"
    )
    val checkedStates = remember { mutableStateListOf(false, false, false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("3rd Group's Dialog") },
        text = {
            Column {
                members.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { isChecked ->
                                checkedStates[index] = isChecked
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
            TextButton(onClick = {
                Toast.makeText(context, "You clicked OK", Toast.LENGTH_SHORT).show()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                Toast.makeText(context, "You closed the dialog window", Toast.LENGTH_SHORT).show()
                onDismiss()
            }) {
                Text("Close")
            }
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    FirstThingTheme {
//        Column() {
//            Greeting("Android")
//            Button(onClick = { /*TODO*/ }) {
//                Text("Add")
//            }
//        }
//    }
//}