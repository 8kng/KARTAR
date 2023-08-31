package com.example.myapplication.controller

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class AuthViewModel: ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordVisibility = mutableStateOf(false)
    val isEmailValid = mutableStateOf(false)
    val isPasswordValid = mutableStateOf(false)
    val allowShowDialog = mutableStateOf(false)

    private fun onEmailChanged(newValue: String) {
        email.value = newValue
    }
    fun onPasswordChanged(newValue: String) {
        password.value = newValue
    }
    fun onEmailValid(newValue: String) {
        val isValid = newValue.all {
            (it in 'a'..'z') ||
            (it in 'A'..'Z') ||
            (it in '0'..'9') ||
            it == '@' ||
            it == '.' ||
            it == '-' ||
            it == '_'
        }
        if (isValid) {
            onEmailChanged(newValue)
        }
    }

    fun signInButtonClick() {
        isEmailValid.value = !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        isPasswordValid.value = password.value.length < 6 || password.value.length > 12

        allowShowDialog.value = !isEmailValid.value && !isPasswordValid.value
    }

    fun signout() {
        FirebaseAuth.getInstance().signOut()
    }
}