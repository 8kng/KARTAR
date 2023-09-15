package com.example.myapplication.controller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SoloPlayViewModel : ViewModel() {
    //遊ぶかるたの選択
    val playKartaUid = mutableStateOf("")
    val playKartaTitle = mutableStateOf("")
}