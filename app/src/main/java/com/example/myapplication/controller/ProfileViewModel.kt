package com.example.myapplication.controller

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.theme.LiteGreen

class ProfileViewModel : ViewModel(){
    val userName = mutableStateOf("")
    val imageUri = mutableStateOf<Uri?>(null)
    val circleColor = mutableStateOf<Color>(LiteGreen)
    val isUserNameValid = mutableStateOf(false)
    private val isUserIconValid = mutableStateOf(false)

    fun onUserNameChanged(newValue: String) {
        userName.value = newValue
    }

    fun onValidCheck(context: Context) {
        Log.d("^^", "^^")
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
            editor.putString(context.getString(R.string.UserName), userName.value)
            editor.putString(context.getString(R.string.imageIcon), imageUri.value.toString())
            editor.apply()
            val name = sharedPref.getString(context.getString(R.string.UserName), "^^").toString()
            Log.d("^^", name)
        }
    }
}