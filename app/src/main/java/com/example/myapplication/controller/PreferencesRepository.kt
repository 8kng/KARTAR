package com.example.myapplication.controller

import android.content.SharedPreferences
import android.net.Uri

class PreferencesRepository(private val sharedPreferences: SharedPreferences) {
    fun profileSetUp(userName: String, imageIcon: Uri){
        with(sharedPreferences.edit()) {
            putString("userName", userName)
            putString("imageIcon", imageIcon.toString())
        }
    }
}