package com.example.myapplication.controller.singleton

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object ConstantsSingleton {
    /*ボタンサイズ*/
    val standardButtonModifier: Modifier = Modifier.height(150.dp).width(250.dp)
    val widthButtonModifier: Modifier = Modifier.height(50.dp).width(250.dp)
    /*ボタンのテキストサイズ*/
    const val standardButtonText: Int = 34
    const val widthButtonText: Int = 18
}