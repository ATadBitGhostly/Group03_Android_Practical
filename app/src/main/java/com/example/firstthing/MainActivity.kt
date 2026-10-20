package com.example.firstthing

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.example.firstthing.ui.theme.FirstThingTheme
import kotlinx.coroutines.launch

enum class ThemeOption { SYSTEM, LIGHT, DARK }
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val themeSelection by getThemeOption(context).collectAsState(initial = ThemeOption.SYSTEM)

            // Determine whether dark theme is active
            val isDarkTheme = when (themeSelection) {
                ThemeOption.LIGHT -> false
                ThemeOption.DARK -> true
                ThemeOption.SYSTEM -> isSystemInDarkTheme()
            }
            FirstThingTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Place dropdown in top bar or screen header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            ThemeDropdownMenu(
                                selectedTheme = themeSelection,
                                onThemeSelected = { newTheme ->
                                    scope.launch {
                                        saveThemeOption(context, newTheme)
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    FirstActivityScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ThemeDropdownMenu(
    selectedTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Simple TextButton or IconButton with Unicode text (no extra icon library needed)
        TextButton(onClick = { expanded = true }) {
            Text("Theme ▾")
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("System Default") },
                onClick = {
                    onThemeSelected(ThemeOption.SYSTEM)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Light") },
                onClick = {
                    onThemeSelected(ThemeOption.LIGHT)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Dark") },
                onClick = {
                    onThemeSelected(ThemeOption.DARK)
                    expanded = false
                }
            )
        }
    }
}
@Composable
fun FirstActivityScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val savedText by getInputText(context).collectAsState(initial = "")
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(savedText) {
        if (inputText.isEmpty() && savedText.isNotEmpty()) {
            inputText = savedText
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "1-st Activity")

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Put text here") }
        )

        Button(onClick = {
            scope.launch {
                saveInputText(context, inputText)
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save")
        }

        Button(onClick = {
            context.startActivity(Intent(context, SecondActivity::class.java))
        }) {
            Text("Go to 2-nd")
        }

        Button(onClick = { showDialog = true }) {
            Text("Dialog")
        }
        CalculatorScreen()
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

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier){
    var display by rememberSaveable { mutableStateOf("0") }
    var firstOperand by rememberSaveable { mutableStateOf<Double?>(null) }
    var pendingOperation by rememberSaveable { mutableStateOf<String?>(null) }
    var isNewInput by rememberSaveable { mutableStateOf(true) }
    var memoryValue by rememberSaveable { mutableStateOf<Double?>(null) }

    fun formatRes(value: Double): String =
        if (value.isNaN() || value.isInfinite()) "Error"
        else if (value % 1 == 0.0) value.toInt().toString()
        else value.toString()


    fun calculate(): Double? {
        val first = firstOperand ?: return null
        val second = display.toDoubleOrNull() ?: return null
        return when (pendingOperation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> if (second != 0.0) first / second else Double.NaN
            else -> second
        }
    }

    fun onDigitClick(digit: String) {
        if(isNewInput || display == "0"){
            display = digit
            isNewInput = false
        } else {
            display += digit
        }
    }

    fun onDecimalClick() {
        if (isNewInput) {
            display = "0."
            isNewInput = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    fun onOperatorClick(op: String) {
        if (pendingOperation != null && !isNewInput) {
            calculate()?.let { display = formatRes(it) }
        }
        firstOperand = display.toDoubleOrNull()
        pendingOperation = op
        isNewInput = true
    }

    fun onEqualClick() {
        val res = calculate() ?: return
        display = formatRes(res)
        firstOperand = null
        pendingOperation = null
        isNewInput = true
    }

    fun onClearClick() {
        display = "0"
        firstOperand = null
        pendingOperation = null
        isNewInput = true
    }

    fun onMemorySave() {
        display.toDoubleOrNull()?.let {
            memoryValue = it
        }
    }

    fun onMemoryRead() {
        memoryValue?.let {saved ->
            display = formatRes(saved)
            isNewInput = false
        }
    }

    fun onMemoryClear() {
        memoryValue = null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calculator/Practical 4", style = MaterialTheme.typography.titleMedium)

        Text(
            text = display,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.End
        )

        Text(
            text = if (memoryValue != null) "Mem: $memoryValue" else "Mem: [Empty]",
            style = MaterialTheme.typography.bodySmall
        )
        val rowsz = listOf(
            listOf("MS", "MR", "MC", "C"),
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )
        rowsz.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { key ->
                    Button(onClick = {
                        when (key) {
                            "MS" -> onMemorySave()
                            "MR" -> onMemoryRead()
                            "MC" -> onMemoryClear()
                            "C" -> onClearClick()
                            "." -> onDecimalClick()
                            "=" -> onEqualClick()
                            "+", "-", "*", "/" -> onOperatorClick(key)
                            else -> onDigitClick(key)
                        }
                    }) { Text(key) }
                }
            }
        }

//        // Memory Row
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onMemorySave() }) { Text("MS") }
//            Button(onClick = { onMemoryRead() }) { Text("MR") }
//            Button(onClick = { onMemoryClear() }) { Text("MC") }
//            Button(onClick = { onClearClick() }) { Text("C") }
//        }
//
//        // Keypad Rows
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onDigitClick("7") }) { Text("7") }
//            Button(onClick = { onDigitClick("8") }) { Text("8") }
//            Button(onClick = { onDigitClick("9") }) { Text("9") }
//            Button(onClick = { onOperatorClick("/") }) { Text("/") }
//        }
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onDigitClick("4") }) { Text("4") }
//            Button(onClick = { onDigitClick("5") }) { Text("5") }
//            Button(onClick = { onDigitClick("6") }) { Text("6") }
//            Button(onClick = { onOperatorClick("*") }) { Text("*") }
//        }
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onDigitClick("1") }) { Text("1") }
//            Button(onClick = { onDigitClick("2") }) { Text("2") }
//            Button(onClick = { onDigitClick("3") }) { Text("3") }
//            Button(onClick = { onOperatorClick("-") }) { Text("-") }
//        }
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onDigitClick("0") }) { Text("0") }
//            Button(onClick = { onDecimalClick() }) { Text(".") }
//            Button(onClick = { onEqualClick() }) { Text("=") }
//            Button(onClick = { onOperatorClick("+") }) { Text("+") }
//        }
    }
}