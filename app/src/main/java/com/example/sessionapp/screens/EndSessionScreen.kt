package com.example.sessionapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.sessionapp.data.model.Session

@Composable
fun EndSessionScreen(onSave: (Session) -> Unit) {
    var sessionId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = sessionId, onValueChange = { sessionId = it }, label = { Text("Session ID") })
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("Age") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (sessionId.isNotEmpty() && name.isNotEmpty() && age.isNotEmpty()) {
                onSave(Session(sessionId, name, age.toInt()))
                Toast.makeText(context, "Session Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save Session")
        }
    }
}

