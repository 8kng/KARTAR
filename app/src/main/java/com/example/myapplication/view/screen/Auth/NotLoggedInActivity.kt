package com.example.myapplication.view.screen.Auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

class NotLoggedInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AuthLoginScreen()
        }
    }
}

@Composable
fun AuthLoginScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            Spacer(modifier = Modifier.height(150.dp))
            Image(
                painter = painterResource(id = R.drawable.kartar_logo),
                contentDescription = "KARTARのロゴ",
                modifier = Modifier
                    .width(320.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(100.dp))
            SignInButton()
            Spacer(modifier = Modifier.height(10.dp))
            LogInTextButton()
        }
    }
}

@Preview
@Composable
fun AuthLoginScreenPreview() {
    AuthLoginScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInButton(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.shadow(10.dp),
        onClick = {
            /*TODO:クリック時にサインイン画面へ移動する*/
        }
    ) {
        Box(
            modifier
                .border(
                    width = 4.dp,
                    color = Color(124, 194, 142, 255)
                )
                .height(46.dp)
                .background(Color(255, 230, 153, 255))
                .width(260.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "サインイン",
                modifier = Modifier,
                color = Color(84, 130, 95, 255),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun LogInTextButton() {
    TextButton(
        onClick = {
        /*TODO:ログイン画面へ移動*/
        }
    ) {
        Text(
            text = "ログインはこちらから",
            modifier = Modifier,
            color = Color(84, 130, 95, 255),
            fontSize = 16.sp,
        )
    }
}