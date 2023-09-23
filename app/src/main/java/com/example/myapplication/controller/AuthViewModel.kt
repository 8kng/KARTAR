package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.controller.singleton.FirebaseSingleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordVisibility = mutableStateOf(false)
    val isEmailValid = mutableStateOf(false)
    val isPasswordValid = mutableStateOf(false)
    val allowShowDialog = mutableStateOf(false)
    val showProcessIndicator = mutableStateOf(false)

    //ユーザのメールアドレス入力処理
    fun onEmailChange(newValue: String) {
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
            email.value = newValue
        }
    }

    //ユーザのパスワード入力処理
    fun onPasswordChanged(newValue: String) {
        val isAlphaNumeric = newValue.all { it.isLetterOrDigit() }
        val isBackspacePressed = newValue.length < password.value.length
        if (isAlphaNumeric && newValue.length <= 12 || isBackspacePressed) {
            password.value = newValue
        }
    }

    //サーバに入力した情報を新規登録してサインインする処理
    fun signInUser(context: Activity, navController: NavController) {
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener(context) { task ->
                    //登録成功時の処理
                    if (task.isSuccessful) {
                        allowShowDialog.value = false
                        Toast.makeText(context, "登録が完了しました!", Toast.LENGTH_SHORT).show()
                        //ユーザ情報入力画面に移動
                        navController.navigate("profileSetup") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        //登録失敗の処理
                    } else if (task.exception?.message.toString() == "The email address is already in use by another account.") {
                        allowShowDialog.value = false
                        Toast.makeText(context, "既に登録されています...", Toast.LENGTH_SHORT).show()
                    } else {
                        allowShowDialog.value = false
                        Toast.makeText(context, "登録が失敗しました...", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, "登録が失敗しました...", Toast.LENGTH_SHORT).show()
        }
    }

    fun onValidCheck(): Boolean {
        isEmailValid.value = !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        isPasswordValid.value = password.value.length < 6 || password.value.length > 12

        allowShowDialog.value = !isEmailValid.value && !isPasswordValid.value
        return !isEmailValid.value && !isPasswordValid.value
    }


    fun loginUser(navController: NavController, context: Activity) {
        viewModelScope.launch {
            showProcessIndicator.value = true
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful) {
                            val uid = task.result.user?.uid
                            //サーバにあるユーザ情報をローカルに保存
                            if (uid != null) {
                                FirebaseFirestore.getInstance()
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
                                            navController.navigate("home") {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            }
                                        }
                                    }
                            }
                            showProcessIndicator.value = false
                        } else {
                            Toast.makeText(context, "ログインが失敗しました...", Toast.LENGTH_SHORT).show()
                            Log.d("エラー", "failed")
                            showProcessIndicator.value = false
                        }
                    }.addOnFailureListener {
                        showProcessIndicator.value = false
                    }
            } catch (e:Exception) {
                Log.d("エラー", e.message.toString())
                Toast.makeText(context, "ログインが失敗しました...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signOutUser(navController: NavController, context: Context) {
        /*ユーザのサインアウト*/
        FirebaseSingleton.userSignOut()
        /*ローカルに記録していたユーザの情報削除*/
        val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.UserInformation), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            commit()
        }
        /*未ログイン画面へ遷移*/
        navController.navigate("notLoggedIn") {
            popUpTo(navController.graph.startDestDisplayName) { inclusive = true }
        }
    }
}