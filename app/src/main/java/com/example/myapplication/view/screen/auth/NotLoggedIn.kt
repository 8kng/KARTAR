package com.example.myapplication.view.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.view.widget.button.ButtonContent

@Composable
fun NotLoggedInScreen(navController: NavController) {
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
            SignInButton(navController)
            Spacer(modifier = Modifier.height(10.dp))
            LogInTextButton(navController = navController)
        }
    }
}

@Composable
fun SignInButton(navController: NavController) {
    ButtonContent(
        modifier = Modifier.height(46.dp).width(260.dp),
        border = 4,
        onClick = { navController.navigate("signin") },
        text = "サインイン",
        fontSize = 18,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun LogInTextButton(navController: NavController) {
    TextButton(
        onClick = {
            navController.navigate("login")
        }
    ) {
        Text(
            text = "ログインはこちらから",
            modifier = Modifier,
            color = DarkGreen,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.kiwimaru_medium))
        )
    }
}

@Preview
@Composable
fun AuthLoginScreenPreview() {
    NotLoggedInScreen(navController = rememberNavController())
}