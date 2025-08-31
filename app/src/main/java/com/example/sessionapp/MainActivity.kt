package com.example.sessionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sessionapp.ui.screens.CameraScreen
import com.example.sessionapp.ui.screens.EndSessionScreen
import com.example.sessionapp.ui.theme.SessionAppTheme
import com.example.sessionapp.data.model.Session
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SessionAppTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    var showCamera by remember { mutableStateOf(true) } // Start with camera screen
    var lastSession by remember { mutableStateOf<Session?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (showCamera) {
            CameraScreen { imageCapture ->
                // You can handle image saved logic here
                showCamera = false // Move to EndSessionScreen after capture
            }
        } else {
            EndSessionScreen { session ->
                lastSession = session
                showCamera = true // Go back to camera or next flow
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    SessionAppTheme {
        MainAppContent()
    }
}

//
//package com.example.sessionapp
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import com.example.sessionapp.screens.CameraScreen
//import com.example.sessionapp.ui.CameraScreen
//import com.example.sessionapp.ui.theme.SessionAppTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SessionAppTheme {
//                Surface(color = MaterialTheme.colorScheme.background) {
//                    CameraScreen() // Call kar raha hu yaha
//                }
//            }
//        }
//    }
//}

