package com.example.myapplication.view.screen.auth

import android.app.Activity
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow
import com.example.myapplication.view.widget.button.OnValidCheckButton
import com.example.myapplication.view.widget.textField.EmailTextField
import com.example.myapplication.view.widget.textField.PasswordTextField

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = AuthViewModel()
        setContent{
            SignInScreen(authViewModel)
            if (authViewModel.allowShowDialog.value) {
                CheckAlertDialog(authViewModel = authViewModel)
            }
        }
    }
}

@Composable
fun SignInScreen(authViewModel: AuthViewModel = AuthViewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "メールアドレス&パスワードを入力してください"
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
                authViewModel.onValidCheck()
            }
        }
    }
}

@Preview
@Composable
fun SignInScreenView() {
    SignInScreen()
}

@Composable
fun CheckAlertDialog(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { authViewModel.allowShowDialog.value = false },
        title = { Text(text = "最終確認", color = DarkGreen)},
        text = {
               Column {
                   Spacer(modifier = Modifier.height(20.dp))
                   Text(text = "入力した情報で登録します", color = Grey)
                   Spacer(modifier = Modifier.height(10.dp))
                   Text(text = "よろしいでしょうか？", color = Grey)
               }
        },
        confirmButton = {
            TextButton(onClick = {
                authViewModel.allowShowDialog.value = false
            }) {
                Text(text = "NO", color = Grey2, fontSize = 16.sp)
            }
            TextButton(
                onClick = { authViewModel.signInUser(context as Activity)}
            ) {
                Text(text = "OK", color = DarkGreen, fontSize = 16.sp)
            }
        }
    )
}