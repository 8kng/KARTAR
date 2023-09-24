package com.example.myapplication.controller.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.controller.ProfileViewModel

class AuthViewModelFactory (private val profileViewModel: ProfileViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(profileViewModel = profileViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}