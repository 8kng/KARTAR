package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.example.myapplication.MainActivity
import com.example.myapplication.view.screen.auth.NotLoggedInActivity
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

    fun signInUser(context: Activity) {
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        allowShowDialog.value = false
                        Toast.makeText(context, "登録が完了しました!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        context.finish()
                    } else if (task.exception?.message.toString() == "The email address is already in use by another account.") {
                        allowShowDialog.value = false
                        Toast.makeText(context, "既に登録されています...", Toast.LENGTH_SHORT).show()
                    } else {
                        allowShowDialog.value = false
                        Toast.makeText(context, "登録が失敗しました...", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Log.d("登録失敗", e.message.toString())
        }
    }

    fun signout() {
        FirebaseAuth.getInstance().signOut()
    }
}