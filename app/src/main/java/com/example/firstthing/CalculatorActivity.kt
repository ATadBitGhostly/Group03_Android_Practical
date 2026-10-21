package com.example.firstthing

import android.R.attr.onClick
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstthing.ui.theme.FirstThingTheme

class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstThingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Calculator(
                        name = "Calculator",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Calculator(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var display by remember { mutableStateOf("0") }
    var specialNumber by remember { mutableStateOf<Double?>(null) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    var isNewInput by remember { mutableStateOf(true) }
    var memoryValue by remember { mutableStateOf<Double?>(null) }

    fun formatRes(value: Double): String =
        if (value.isNaN() || value.isInfinite()) "Error"
        else if (value % 1 == 0.0) value.toLong().toString()
        else value.toString()

    fun calculate(): Double? {
        val first = specialNumber ?: return null
        val second = display.toDoubleOrNull() ?: return null
        return when (pendingOperator) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> first / second
            else -> second
        }
    }

    fun onDigitClick(digit: String) {
        if (isNewInput || display == "0") {
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
        if (pendingOperator != null && !isNewInput) {
            calculate()?.let { display = formatRes(it) }
        }
        specialNumber = display.toDoubleOrNull()
        pendingOperator = op
        isNewInput = true
    }

    fun onEqualClick() {
        val res = calculate() ?: return
        display = formatRes(res)
        specialNumber = null
        pendingOperator = null
        isNewInput = true
    }

    fun onClearClick() {
        display = "0"
        specialNumber = null
        pendingOperator = null
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "Calculator Activity!"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = display,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.End
        )

        Text(
            text = if (memoryValue != null) "Mem: $memoryValue" else "Mem: Empty",
            style = MaterialTheme.typography.bodySmall,
        )
        val rows = listOf(
            listOf("MS", "MR", "MC", "C"),
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )

        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { item ->
                    Button(onClick = {
                        when (item) {
                            "MS" -> onMemorySave()
                            "MR" -> onMemoryRead()
                            "MC" -> onMemoryClear()
                            "C" -> onClearClick()
                            "." -> onDecimalClick()
                            "=" -> onEqualClick()
                            "+", "-", "*", "/" -> onOperatorClick(item)
                            else -> onDigitClick(item)
                        }
                    }) {Text(item)}
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        }) {
            Text(
                text = "Go back"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    FirstThingTheme {
        Calculator("Calculator")
    }
}