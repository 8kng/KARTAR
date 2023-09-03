package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.MyApplicationTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                HomeActivityScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeActivityScreen() {
    Scaffold(
        topBar = { KARTARAppBar() }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                PlayKARTAButton(modifier = Modifier)
                Spacer(modifier = Modifier.height(100.dp))
                EFUDAButton(modifier = Modifier)
            }
        }
    }
}

@Composable
@Preview
fun HomeActivityScreenView() {
    HomeActivityScreen()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KARTARAppBar() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.UserInformation),
        Context.MODE_PRIVATE
    )
    val imageUrl = sharedPref.getString(context.getString(R.string.imageIcon), "")
    TopAppBar(
        modifier = Modifier.padding(
            top = 14.dp,
            end = 20.dp
        ),
        title = { },
        actions = {
            AsyncImage(
                model = imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .border(
                        width = 0.5.dp,
                        color = LiteGreen,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .size(50.dp)
            )
        }
    )
}

@Composable
fun ButtonContent(modifier: Modifier = Modifier, text: String, fontSize: Int = 40) {
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
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 50.sp,
            modifier = modifier
        )
    }
}