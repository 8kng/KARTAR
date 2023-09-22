package com.example.myapplication.controller

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.FirebaseSingleton

class MainViewModel(context: Context): ViewModel(){
    private val userInformationSharedPreferences: SharedPreferences? = context.getSharedPreferences("UserInformation", Context.MODE_PRIVATE)

    /**ユーザの名前をローカルから取得**/
    private fun getUserName(): String {
        return userInformationSharedPreferences?.getString("UserName", "").toString()
    }

    /**起動時の画面遷移の設定**/
    fun getStartDestination(): String {
        return try {
            FirebaseSingleton.ensureLoggedIn()
            if (FirebaseSingleton.currentUid() != null) {
                val userName = getUserName()
                if (userName == "") "profileSetUp" else "home"
            } else {
                "notLoggedIn"
            }
        } catch (e: Exception) {
            Log.d("エラー", "ログイン失敗:${e.message}")
            "notLoggedIn"
        }
    }
}