package com.example.myapplication.view.screen.auth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.view.widget.button.ButtonContent
import com.example.myapplication.view.widget.button.OnValidCheckButton
import com.example.myapplication.view.widget.textField.EmailTextField
import com.example.myapplication.view.widget.textField.PasswordTextField

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "登録した\nメールアドレス&パスワードを入力してください"
                )
                Spacer(modifier = Modifier.height(50.dp))
                EmailTextField(authViewModel)
                if (authViewModel.isEmailValid.value) {
                    Text(
                        text = "有効なメールアドレスを入力してください",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                PasswordTextField(authViewModel)
                if (authViewModel.isPasswordValid.value) {
                    Text(
                        text = "有効なパスワードを入力してください",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(230.dp))
                OnValidButton(navController = navController, authViewModel = authViewModel)
            }
            //サーバ処理中に表示するインディケーター
            if (authViewModel.showProcessIndicator.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = LiteGreen)
                }
            }
        }
    }
}

@Composable
private fun OnValidButton(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    ButtonContent(
        modifier = Modifier
            .height(50.dp)
            .width(250.dp),
        onClick = {
            val onValid = authViewModel.onValidCheck()
            if (onValid) {
                authViewModel.loginUser(
                    navController = navController,
                    context = context as Activity
                )
            }
                  },
        text = "OK",
        border = 4,
        fontSize = 18,
        fontWeight = FontWeight.Normal
    )
}

@Preview
@Composable
fun LoginScreenView() {
    LoginScreen(rememberNavController(), AuthViewModel())
}
