package com.example.myapplication.view.screen.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow
import com.example.myapplication.theme.Yellow2
import com.google.firebase.auth.FirebaseAuth

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
            CompleteButton(Modifier, authViewModel)
        }
    }
}

@Preview
@Composable
fun SignInScreenView() {
    SignInScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(authViewModel: AuthViewModel = AuthViewModel()) {
    OutlinedTextField(
        value = authViewModel.email.value,
        modifier = Modifier,
        onValueChange = { newValue ->
            authViewModel.onEmailValid(newValue)
        },
        isError = authViewModel.isEmailValid.value,
        singleLine = true,
        placeholder = {
            Text(
                text = "メールアドレスを入力してください",
                fontSize = 12.sp,
                color = Grey2
            )
        },
        label = {
            Text(
                text = "メールアドレス",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Grey,
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = LiteGreen,
            containerColor = Yellow2
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(authViewModel: AuthViewModel = AuthViewModel()) {
    OutlinedTextField(
        value = authViewModel.password.value,
        onValueChange = { newValue ->
            val isAlphaNumeric = newValue.all { it.isLetterOrDigit() }
            val isBackspacePressed = newValue.length < authViewModel.password.value.length
            if (isAlphaNumeric && authViewModel.password.value.length <= 12 || isBackspacePressed) {
                authViewModel.onPasswordChanged(newValue)
            }
        },
        singleLine = true,
        placeholder = {
            Text(
                text = "英数字で6～12文字で入力してください",
                fontSize = 12.sp,
                color = Grey2
            )
        },
        isError = authViewModel.isPasswordValid.value,
        label = {
            Text(
                text = "パスワード",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Grey,
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = LiteGreen,
            containerColor = Yellow2
        ),
        trailingIcon = {
            val image = if (authViewModel.passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(
                onClick = { authViewModel.passwordVisibility.value = !authViewModel.passwordVisibility.value },
            ) {
                Icon(imageVector = image, contentDescription = "Toggle password visibility")
            }
        },
        visualTransformation = if (authViewModel.passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteButton(modifier: Modifier = Modifier, authViewModel: AuthViewModel = AuthViewModel()) {
    Surface(
        modifier = modifier.shadow(10.dp),
        onClick = { authViewModel.signInButtonClick() }
    ) {
        Box(
            modifier
                .border(width = 4.dp, color = LiteGreen)
                .height(46.dp)
                .background(Yellow)
                .width(260.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "OK",
                modifier = Modifier,
                color = DarkGreen,
                fontSize = 16.sp,
            )
        }
    }
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
                onClick = {
                    try {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(authViewModel.email.value, authViewModel.password.value)
                            .addOnCompleteListener(context as Activity) { task ->
                                if (task.isSuccessful) {
                                    authViewModel.allowShowDialog.value = false
                                    Toast.makeText(context, "登録が完了しました!", Toast.LENGTH_SHORT).show()
                                } else if (task.exception?.message.toString() == "The email address is already in use by another account.") {
                                    authViewModel.allowShowDialog.value = false
                                    Toast.makeText(context, "既に登録されています...", Toast.LENGTH_SHORT).show()
                                } else {
                                    authViewModel.allowShowDialog.value = false
                                    Toast.makeText(context, "登録が失敗しました...", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } catch (e: Exception) {
                        Log.d("登録失敗", e.message.toString())
                    }
                }
            ) {
                Text(text = "OK", color = DarkGreen, fontSize = 16.sp)
            }
        }
    )
}