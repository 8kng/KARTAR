package com.example.myapplication.controller

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.KARTAData

class CreateViewModel : ViewModel() {
    val searchBoxText = mutableStateOf("")
    val isSearchBoxTextValid = mutableStateOf(false)
    //オリジナルかるた作成用の変数
    val kartaDataList = mutableStateOf(listOf(KARTAData("", "")))

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

    fun onChangeYomifuda(newValue: String, index: Int) {
        //if (kartaDataList.value?.get(index)?.yomifuda?.length  == 1 && newValue.isEmpty()) {
        /*
        Log.d("onChange1", "$index, $newValue")
        Log.d("onChange2", "$index, ${kartaDataList.value[index].yomifuda}")
        if (newValue.length >= 20) {
            kartaDataList.value[index].yomifuda = newValue
            kartaDataList.value = kartaDataList.value
        }
         */
        val currentList = kartaDataList.value
        if (index in currentList.indices) {
            val updateItem = currentList[index].copy(yomifuda = newValue)
            val updateList = currentList.toMutableList().apply {
                this[index] = updateItem
            }
            kartaDataList.value = updateList
        }
    }
}