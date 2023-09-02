package com.example.myapplication.view.widget.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow2

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