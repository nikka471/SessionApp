package com.example.sessionapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

// Helper function to convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): Bitmap {
    // Assuming the ImageProxy is in NV21 format (for CameraX)
    val planes = this.planes
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    // Convert byte array to Bitmap (NV21 format decoding)
    val yuvImage = android.graphics.YuvImage(bytes, android.graphics.ImageFormat.NV21, this.width, this.height, null)
    val outputStream = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, this.width, this.height), 100, outputStream)

    val byteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

// Function to save the image in external storage
fun saveImage(image: ImageProxy, sessionId: String, context: Context) {
    val bitmap = image.toBitmap()
    val timestamp = System.currentTimeMillis()
    val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Sessions/$sessionId")
    if (!directory.exists()) directory.mkdirs()

    val imageFile = File(directory, "IMG_$timestamp.jpg")
    val outputStream = FileOutputStream(imageFile)

    // Compress the Bitmap to JPEG and save it
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    image.close()
}
