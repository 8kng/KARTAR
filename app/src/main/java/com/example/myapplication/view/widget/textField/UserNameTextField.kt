package com.example.myapplication.view.widget.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.ButtonContainer
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameTextField(profileViewModel: ProfileViewModel) {
    OutlinedTextField(
        value = profileViewModel.userName.value,
        onValueChange = { newValue -> profileViewModel.onUserNameChanged(newValue) },
        singleLine = true,
        placeholder = {
            Text(
                text = "1～16文字で入力してください",
                fontSize = 12.sp,
                color = Grey2
            )
        },
        isError = profileViewModel.isUserNameValid.value,
        label = {
            Text(
                text = "ユーザネーム",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Grey,
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = LiteGreen,
            containerColor = ButtonContainer.copy(alpha = 0.5f)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
    )
}