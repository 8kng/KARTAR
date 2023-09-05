package com.example.myapplication.controller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.KARTAData

class CreateViewModel : ViewModel() {
    val searchBoxText = mutableStateOf("")
    val isSearchBoxTextValid = mutableStateOf(false)
    //オリジナルかるた作成用の変数
    val kartaDataList = MutableLiveData<List<KARTAData>>()

    init {
        //かるたデータを初期化
        val hiraganaList = listOf("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ")
        kartaDataList.value = hiraganaList.map { KARTAData(efuda = "", yomifuda = it) }
    }

    fun onSearchBoxChange(newValue: String) {
        if (newValue.length  < 20) {
            searchBoxText.value = newValue
        }
    }
}