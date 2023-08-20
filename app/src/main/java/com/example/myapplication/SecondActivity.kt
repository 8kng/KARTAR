package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.theme.MyApplicationTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            PlayKARTAButton(modifier = Modifier)
                            Spacer(modifier = Modifier.height(100.dp))
                            EFUDAButton(modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayKARTAButton(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        onClick = {}
    ) {
        Column {
            ButtonContent(text = "かるた\nで遊ぶ")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EFUDAButton(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        onClick = {}
    ) {
        Column {
            ButtonContent(text = "絵札")
        }
    }
}

@Composable
fun ButtonContent(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier
            .border(
                width = 8.dp,
                color = Color(124, 194, 142, 255)
            )
            .height(120.dp)
            .background(Color(255, 230, 153, 255))
            .width(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(65, 65, 65),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 50.sp,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlayKARTAButtonPreview() {
    PlayKARTAButton()
}

@Preview(showBackground = true)
@Composable
fun EFUDAButtonPreview() {
    EFUDAButton()
}