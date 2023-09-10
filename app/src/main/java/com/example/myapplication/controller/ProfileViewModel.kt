package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.theme.LiteGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel(){
    //SharedPreferenceからユーザ情報を取得
    private val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.UserInformation), Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()
    //ユーザ情報の変数
    val userName = mutableStateOf(sharedPref.getString(context.getString(R.string.UserName), "ユーザネーム").toString())
    val imageUri = mutableStateOf(sharedPref.getString(context.getString(R.string.imageIcon), "")?.toUri())
    val localImageUri = mutableStateOf<Uri?>(null)
    val isUserNameValid = mutableStateOf(false)
    val isUserIconValid = mutableStateOf(false)
    val circleColor = mutableStateOf(LiteGreen)
    val showProcessIndicator = mutableStateOf(false)
    val showUserNameDialog = mutableStateOf(false)
    val showSignOutDialog = mutableStateOf(false)

    //ユーザ名の入力処理
    fun onUserNameChanged(newValue: String) {
        if (newValue.length < 17) {
            userName.value = newValue
        }
    }

    //ユーザ名・アイコンをサーバに登録
    fun registerUserInformation(context: Context, navController: NavController) {
        //ユーザ名・アイコンのvalidチェック
        isUserNameValid.value = userName.value.isEmpty() || userName.value.length > 16
        isUserIconValid.value = localImageUri.value == null
        //ユーザアイコンの色
        if (isUserIconValid.value) {
            circleColor.value = Color.Red
        } else {
            circleColor.value = LiteGreen
        }
        //入力に問題がない場合の処理
        if (!isUserNameValid.value && !isUserIconValid.value) {
            viewModelScope.launch {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                showProcessIndicator.value = true
                try {
                    //アイコン画像をstorageに保存
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("iconImages/${uid}")
                    val uploadTask = localImageUri.value?.let { imageRef.putFile(it) }
                    uploadTask?.addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            if (uid != null)  {
                                //サーバに情報を書き込み
                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(uid)
                                    .set(hashMapOf(
                                        "userName" to userName.value,
                                        "iconImage" to downloadUri
                                    ))
                                //ローカルに情報を書き込み
                                editor.putString(context.getString(R.string.UserName), userName.value)
                                editor.putString(context.getString(R.string.imageIcon), downloadUri.toString())
                                editor.apply()
                                showProcessIndicator.value = false
                                //HomeScreenに移動
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "登録に失敗しました...", Toast.LENGTH_SHORT).show()
                            showProcessIndicator.value = false
                        }
                    }?.addOnFailureListener{ exception ->
                        Log.d("^^", exception.message.toString())
                        Toast.makeText(context, "登録に失敗しました...\nネットワーク環境を確かめてください", Toast.LENGTH_SHORT).show()
                        showProcessIndicator.value = false
                    }
                } catch (e:Exception) {
                    Log.d("e", "エラーでたよ～")
                    showProcessIndicator.value = false
                }
            }
        }
    }

    fun changeUserImageIcon(uri: Uri, context: Context) {
        imageUri.value = uri
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.UserInformation),
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("iconImages/${uid}")
        viewModelScope.launch {
            showProcessIndicator.value = true
            try {
                //アイコン画像をstorageに保存
                val uploadTask = imageUri.value?.let { imageRef.putFile(it) }
                uploadTask?.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        //サーバに情報を書き込み
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .update("iconImage", downloadUri)
                        //ローカルに情報を書き込み
                        editor.putString(context.getString(R.string.imageIcon), downloadUri.toString())
                        editor.apply()
                        updateUserIconFromSharedPref(context)
                        showProcessIndicator.value = false
                    }.addOnFailureListener {
                        Toast.makeText(context, "更新に失敗しました...ネットワーク環境を確かめてください", Toast.LENGTH_SHORT).show()
                        showProcessIndicator.value = false
                    }
                }?.addOnFailureListener{
                    Toast.makeText(context, "更新に失敗しました...", Toast.LENGTH_SHORT).show()
                    showProcessIndicator.value = false
                }
            } catch (e: Exception) {
                Log.d("e", "エラーでたよ～")
                showProcessIndicator.value = false
            }
        }
    }

    //ユーザ名の変更ダイアログ入力処理
    fun onClickUserNameDialog(context: Context) {
        isUserNameValid.value = userName.value.length > 16
        if (userName.value == "") {
            showUserNameDialog.value = false
        } else if (isUserNameValid.value) {
            Toast.makeText(context, "入力にミスがあります", Toast.LENGTH_SHORT).show()
            showUserNameDialog.value = false
        } else {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            viewModelScope.launch {
                try {
                    //サーバに情報を書き込み
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .update("userName", userName.value)
                    //ローカルに情報を書き込み
                    editor.putString(context.getString(R.string.UserName), userName.value)
                    editor.apply()
                    delay(1000)
                    updateUserNameFromSharedPref(context)
                    showUserNameDialog.value = false
                    Toast.makeText(context, "名前を変更しました", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.d("e", "エラーでたよ～")
                    showUserNameDialog.value = false
                }
            }
        }
    }

    //ユーザ情報変更後の変数の値更新処理
    private fun updateUserNameFromSharedPref(context: Context) {
        userName.value = sharedPref.getString(context.getString(R.string.UserName), "ユーザネーム").toString()
    }
    fun updateUserIconFromSharedPref(context: Context) {
        imageUri.value = sharedPref.getString(context.getString(R.string.imageIcon), "")?.toUri()
    }
}