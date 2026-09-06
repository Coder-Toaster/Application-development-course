package com.example.helloworld2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.helloworld2026.ui.theme.HelloWorld2026Theme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column // So I can set the text boxes under each other easily.
import androidx.compose.material3.Button // for button

import androidx.compose.foundation.layout.size



// These are for the battery-% popup
import android.content.Context
import android.os.BatteryManager
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorld2026Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> // This was by default on and it caused the padding to be added on each text element again. Not removing it now cause I dont know what I am doing.
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false)}
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    Column(
        modifier = modifier.padding(
            start = 30.dp,
            top = 40.dp,
            end = 30.dp
        )
    ) {
        Text(
            text = "Patrik Verho",
            style = MaterialTheme.typography.headlineLarge

        )
        Text(
            text = """
                This is my first Android application. I have used AI to help me understand for example
                how to use padding and how to create large headline. I have not in any point copy-pasted
                exercise instructions to the AI so it could do the task for me.     
            """.trimIndent().replace("\n", " "),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp) // modifier (l_case) already contained the padding from parent so the existing padding was applied to the text too.
        )
        Text(
            text = """
                This is the second text field that was required. So now might be good time to tell
                that I used AI to find the trimIndent().replace(...) solution for the text not to be
                indented when I change line on this text field. 
            """.trimIndent().replace("\n", " "),
            modifier = Modifier.padding(top = 20.dp)
        )
        BatteryButton(
            onClick = { showDialog = true }, // This tells that when the button is clicked a popup is shown ("dialog")
            modifier = Modifier
                .padding( top = 30.dp )
                .align( Alignment.CenterHorizontally )
                .size( width = 180.dp, height = 70.dp )
        )
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text("Battery level")
            },
            text = {
                Text("Device battery level is at $batteryLevel%")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorld2026Theme {
        Greeting("Android")
    }
}
@Composable
fun BatteryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text("Show Battery level")
    }
}