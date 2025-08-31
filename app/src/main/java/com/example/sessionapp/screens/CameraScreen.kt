
package com.example.sessionapp.ui.screens

import android.Manifest
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun CameraScreen(onImageCaptured: (File) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as androidx.lifecycle.LifecycleOwner

    // CameraX instances
    var hasCameraPermission by remember { mutableStateOf(false) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val previewView = remember { PreviewView(context) }

    // Runtime permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Request permission on first load
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Bind camera only if permission is granted and PreviewView is ready
    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            previewView.post {
                try {
                    val cameraProvider = ProcessCameraProvider.getInstance(context).get()

                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, "Camera binding failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // Live camera preview
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        }

        // Capture button
        Button(
            onClick = {
                if (!hasCameraPermission) {
                    Toast.makeText(context, "Camera permission not granted", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "IMG_${System.currentTimeMillis()}.jpg"
                )

                val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    outputFileOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            Toast.makeText(context, "Image Captured", Toast.LENGTH_SHORT).show()
                            onImageCaptured(file)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(context, "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Capture Image")
        }
    }
}

//
//package com.example.sessionapp.ui
//
//import android.Manifest
//import android.content.Context
//import android.net.Uri
//import android.os.Environment
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.content.ContextCompat
//import androidx.navigation.NavController
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//
//@Composable
//fun CameraScreen(navController: NavController) {
//    val context = LocalContext.current
//    val imageCapture = remember { ImageCapture.Builder().build() }
//    val imageList = remember { mutableStateListOf<Uri>() } // multiple images
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { granted ->
//            if (!granted) {
//                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    )
//
//    LaunchedEffect(Unit) {
//        launcher.launch(Manifest.permission.CAMERA)
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Camera Preview (dummy placeholder text for now, replace with PreviewView if working)
//        Text("Camera Preview Here", modifier = Modifier.padding(20.dp))
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth().padding(16.dp)
//        ) {
//            Button(onClick = {
//                takePhoto(context, imageCapture, imageList)
//            }) {
//                Text("Capture Photo")
//            }
//
//            Button(onClick = {
//                // जब session खत्म हो → form screen पर redirect
//                navController.navigate("form")
//            }) {
//                Text("End Session")
//            }
//        }
//    }
//}
//
//private fun takePhoto(
//    context: Context,
//    imageCapture: ImageCapture,
//    imageList: MutableList<Uri>
//) {
//    val sessionDir = File(
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//        "SessionApp/Session_${getCurrentDate()}"
//    )
//    if (!sessionDir.exists()) sessionDir.mkdirs()
//
//    val photoFile = File(
//        sessionDir,
//        "IMG_${System.currentTimeMillis()}.jpg"
//    )
//
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//    imageCapture.takePicture(
//        outputOptions,
//        ContextCompat.getMainExecutor(context),
//        object : ImageCapture.OnImageSavedCallback {
//            override fun onError(exc: ImageCaptureException) {
//                Toast.makeText(context, "Capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                val savedUri = Uri.fromFile(photoFile)
//                imageList.add(savedUri)
//                Toast.makeText(context, "Photo saved: $savedUri", Toast.LENGTH_SHORT).show()
//            }
//        }
//    )
//}
//
//private fun getCurrentDate(): String {
//    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//    return sdf.format(Date())
//}




//package com.example.sessionapp.screens
//
//import android.Manifest
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.Toast
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberPermissionState
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun CameraScreen() {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
//    val imageCapture = remember { ImageCapture.Builder().build() }
//
//    var outputUri by remember { mutableStateOf<Uri?>(null) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        if (cameraPermissionState.hasPermission) {
//            AndroidView(
//                factory = { ctx ->
//                    val previewView = PreviewView(ctx).apply {
//                        layoutParams = FrameLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                    }
//
//                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
//                    cameraProviderFuture.addListener({
//                        val cameraProvider = cameraProviderFuture.get()
//
//                        val preview = Preview.Builder().build().also {
//                            it.setSurfaceProvider(previewView.surfaceProvider)
//                        }
//
//                        try {
//                            cameraProvider.unbindAll()
//                            cameraProvider.bindToLifecycle(
//                                lifecycleOwner,
//                                CameraSelector.DEFAULT_BACK_CAMERA,
//                                preview,
//                                imageCapture
//                            )
//                        } catch (e: Exception) {
//                            Log.e("CameraScreen", "Use case binding failed", e)
//                        }
//                    }, ContextCompat.getMainExecutor(ctx))
//
//                    previewView
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//
//            Button(
//                onClick = {
//                    takePhoto(context, imageCapture) { uri ->
//                        outputUri = uri
//                        Toast.makeText(context, "Image saved: $uri", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(16.dp)
//            ) {
//                Text("Capture Image")
//            }
//        } else {
//            Button(
//                onClick = { cameraPermissionState.launchPermissionRequest() },
//                modifier = Modifier.align(Alignment.Center)
//            ) {
//                Text("Grant Camera Permission")
//            }
//        }
//    }
//}
//
//private fun takePhoto(
//    context: Context,
//    imageCapture: ImageCapture,
//    onImageCaptured: (Uri?) -> Unit
//) {
//    val photoFile = File(
//        context.externalMediaDirs.first(),
//        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
//    )
//
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//    imageCapture.takePicture(
//        outputOptions,
//        ContextCompat.getMainExecutor(context),
//        object : ImageCapture.OnImageSavedCallback {
//            override fun onError(exc: ImageCaptureException) {
//                Log.e("CameraScreen", "Photo capture failed: ${exc.message}", exc)
//                onImageCaptured(null)
//            }
//
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                onImageCaptured(Uri.fromFile(photoFile))
//            }
//        }
//    )
//}
