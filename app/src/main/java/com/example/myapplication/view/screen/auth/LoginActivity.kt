package com.example.myapplication.view.screen.auth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.view.widget.button.OnValidCheckButton
import com.example.myapplication.view.widget.textField.EmailTextField
import com.example.myapplication.view.widget.textField.PasswordTextField

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = AuthViewModel()
        setContent{
            LoginScreen(authViewModel)
        }
    }
}

@Composable
fun LoginScreen(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
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
            OnValidCheckButton(Modifier) {
                val onValid = authViewModel.onValidCheck()
                if (onValid) {
                    authViewModel.loginUser(context = context as Activity)
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenView() {
    LoginScreen(AuthViewModel())
}
