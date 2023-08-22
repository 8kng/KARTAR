package com.example.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.createBitmap
import coil.compose.AsyncImage
import com.example.myapplication.theme.MyApplicationTheme
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ImageSaveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/witz-3dcbd.appspot.com/o/news%2Fhome.png?alt=media&token=c36617ee-a9e7-4cc0-90a0-57141af8f860"

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ImageSaveButton()
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSaveButton(modifier: Modifier = Modifier, context: Context = LocalContext.current) {
    Surface(
        modifier = modifier,
        onClick = {
            val imageUrl = "https://firebasestorage.googleapis.com/v0/b/witz-3dcbd.appspot.com/o/news%2Fhome.png?alt=media&token=c36617ee-a9e7-4cc0-90a0-57141af8f860"
            GlobalScope.launch {
                val bitmap = Glide.with(context).asBitmap().load(imageUrl).submit().get()
                val directory = ContextWrapper(context).getDir(
                    "KARTA",
                    Context.MODE_PRIVATE
                )
                val file = File(directory, "imageName.png")
                FileOutputStream(file).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    Log.d("stream", "画像を保存した")
                }
            }
        }
    ) {
        Column {
            ButtonContent(text = "画像を保存する")
        }
    }
}