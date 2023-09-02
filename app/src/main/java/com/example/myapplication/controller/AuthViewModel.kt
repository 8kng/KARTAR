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
import androidx.lifecycle.viewModelScope
import com.example.myapplication.HomeActivity
import com.example.myapplication.ImageSaveActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.view.screen.auth.NotLoggedInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordVisibility = mutableStateOf(false)
    val isEmailValid = mutableStateOf(false)
    val isPasswordValid = mutableStateOf(false)
    val allowShowDialog = mutableStateOf(false)
    val showProcessIndicator = mutableStateOf(false)

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

    fun onValidCheck(): Boolean {
        isEmailValid.value = !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        isPasswordValid.value = password.value.length < 6 || password.value.length > 12

        allowShowDialog.value = !isEmailValid.value && !isPasswordValid.value
        return !isEmailValid.value && !isPasswordValid.value
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
            Toast.makeText(context, "登録が失敗しました...", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginUser(context: Activity) {
        viewModelScope.launch {
            showProcessIndicator.value = true
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful) {
                            val uid = task.result.user?.uid
                            //サーバにあるユーザ情報をローカルに保存
                            if (uid != null) {
                                val userInfo = FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document != null && document.exists()) {
                                            val userName = document.getString("userName")
                                            val userIconImage = document.getString("iconImage")
                                            //情報を基にローカルにユーザ情報を保存
                                            val sharedPref = context.getSharedPreferences(
                                                context.getString(R.string.UserInformation),
                                                Context.MODE_PRIVATE
                                            )
                                            val editor = sharedPref.edit()
                                            editor.putString(context.getString(R.string.UserName), userName.toString())
                                            editor.putString(context.getString(R.string.imageIcon), userIconImage.toString())
                                            editor.apply()
                                            Toast.makeText(context, "ログインが完了しました!", Toast.LENGTH_SHORT).show()
                                            showProcessIndicator.value = false
                                            //HomeActivityに移動
                                            val intent = Intent(context, HomeActivity::class.java)
                                            context.startActivity(intent)
                                            context.finish()
                                        }
                                    }
                            }
                            showProcessIndicator.value = false
                        } else {
                            Toast.makeText(context, "ログインが失敗しました...", Toast.LENGTH_SHORT).show()
                            Log.d("login", "failed")
                            showProcessIndicator.value = false
                        }
                    }.addOnFailureListener {
                        showProcessIndicator.value = false
                    }
            } catch (e:Exception) {
                Toast.makeText(context, "ログインが失敗しました...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signout() {
        FirebaseAuth.getInstance().signOut()
    }
}