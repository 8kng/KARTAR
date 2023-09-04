package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.HomeActivity
import com.example.myapplication.ImageSaveActivity
import com.example.myapplication.R
import com.example.myapplication.theme.LiteGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(){
    val userName = mutableStateOf("")
    val imageUri = mutableStateOf<Uri?>(null)
    val prefUserName = mutableStateOf("ユーザネーム")
    val prefUserIcon = mutableStateOf<Uri?>(null)
    val circleColor = mutableStateOf<Color>(LiteGreen)
    val isUserNameValid = mutableStateOf(false)
    private val isUserIconValid = mutableStateOf(false)
    val showProcessIndicator = mutableStateOf(false)
    val showUserNameDialog = mutableStateOf(false)
    val showSignOutDialog = mutableStateOf(false)

    fun onUserNameChanged(newValue: String) {
        userName.value = newValue
    }

    fun onValidCheck(context: Context) {
        isUserNameValid.value = userName.value.isEmpty() || userName.value.length > 16
        isUserIconValid.value = imageUri.value == null
        if (isUserIconValid.value) {
            circleColor.value = Color.Red
        } else {
            circleColor.value = LiteGreen
        }
        if (!isUserNameValid.value && !isUserIconValid.value) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.UserInformation),
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            //非同期処理開始する～
            viewModelScope.launch {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                showProcessIndicator.value = true
                try {
                    //アイコン画像をstorageに保存
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("iconImages/${uid}")
                    val uploadTask = imageUri.value?.let { imageRef.putFile(it) }
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
                                editor.putString(context.getString(R.string.imageIcon), imageUri.value.toString())
                                editor.apply()
                                showProcessIndicator.value = false
                                val intent = Intent(context, HomeActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "登録に失敗しました...ネットワーク環境を確かめてください", Toast.LENGTH_SHORT).show()
                            showProcessIndicator.value = false
                        }
                    }?.addOnFailureListener{
                        Toast.makeText(context, "登録に失敗しました...ネットワーク環境を確かめてください", Toast.LENGTH_SHORT).show()
                        showProcessIndicator.value = false
                    }
                } catch (e:Exception) {
                    Log.d("e", "エラーでたよ～")
                    showProcessIndicator.value = false
                }
            }
        }
    }

    fun updateUserNameFromSharedPref(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.UserInformation),
            Context.MODE_PRIVATE
        )
        prefUserName.value = sharedPref.getString(context.getString(R.string.UserName), "ユーザネーム").toString()
    }

    fun updateUserIconFromSharedPref(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.UserInformation),
            Context.MODE_PRIVATE
        )
        prefUserIcon.value = sharedPref.getString(context.getString(R.string.imageIcon), "")?.toUri()
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

    fun onClickUserNameDialog(context: Context) {
        isUserNameValid.value = userName.value.length > 16
        if (userName.value == "") {
            showUserNameDialog.value = false
        } else if (isUserNameValid.value) {
            Toast.makeText(context, "入力にミスがあります", Toast.LENGTH_SHORT).show()
            showUserNameDialog.value = false
        } else {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val sharedPref = context.getSharedPreferences(context.getString(R.string.UserInformation), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
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
                    updateUserNameFromSharedPref(context)
                    delay(500)
                    showUserNameDialog.value = false
                    Toast.makeText(context, "名前を変更しました", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.d("e", "エラーでたよ～")
                    showUserNameDialog.value = false
                }
            }
        }
    }
}