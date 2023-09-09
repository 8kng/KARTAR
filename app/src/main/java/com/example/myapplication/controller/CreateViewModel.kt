package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.KARTAData
import com.example.myapplication.view.screen.create.EFUDAActivity
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.system.exitProcess

class CreateViewModel : ViewModel() {
    val searchBoxText = mutableStateOf("")
    val isSearchBoxTextValid = mutableStateOf(false)
    //オリジナルかるた作成用の変数
    val kartaDataList = mutableStateOf(listOf(KARTAData("", "")))
    val hiraganaList = listOf("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ")
    val isYomifudaValid = mutableStateOf(false)
    //タイトルと説明文を入力するダイアログ表示
    val showInputTitleDialog = mutableStateOf(false)
    val kartaTitle = mutableStateOf("")
    val isKartaTitleValid = mutableStateOf(false)
    val kartaDescription = mutableStateOf("")
    val isKartaDescriptionValid = mutableStateOf(false)

    init {
        //かるたデータを初期化
        kartaDataList.value = hiraganaList.map { KARTAData(efuda = "", yomifuda = it) }
    }

    fun onSearchBoxChange(newValue: String) {
        if (newValue.length  < 20) {
            searchBoxText.value = newValue
        }
    }

    //読み札の入力処理
    fun onChangeYomifuda(newValue: String, index: Int) {
        if (newValue.length <= 20) {
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

    fun onChangeEfuda(newValue: Uri, index: Int) {
        val currentList = kartaDataList.value
        if (index in currentList.indices) {
            val updateItem = currentList[index].copy(efuda = newValue.toString())
            val updateList = currentList.toMutableList().apply {
                this[index] = updateItem
            }
            kartaDataList.value = updateList
        }
    }

    //かるた保存ボタンを押した時の処理
    fun onClickSaveButton(context: Context) {
        val allConditionMet = kartaDataList.value.withIndex().all { (index, kartaData) ->
            val firstCharOfYomifuda = kartaData.yomifuda.firstOrNull()
            firstCharOfYomifuda == hiraganaList[index].first() && kartaData.efuda != ""
        }
        if (allConditionMet) {
            showInputTitleDialog.value = true
            isYomifudaValid.value = false
        } else {
            isYomifudaValid.value = true
        }
    }

    /*作成したかるたのタイトル入力処理*/
    fun onChangeKartaTitle(newValue: String) {
        if (newValue.length < 16) {
            kartaTitle.value = newValue
        }
    }

    /*作成したかるたの説明文入力処理*/
    fun onChangeKartaDescription(newValue: String) {
        if (newValue.length < 21) {
            kartaDescription.value = newValue
        }
    }

    /*作成したかるたをローカルに保存する処理*/
    fun saveKartaToLocal(context: Context) {
        isKartaTitleValid.value = kartaTitle.value == ""
        isKartaDescriptionValid.value = kartaDescription.value == ""
        if (!isKartaTitleValid.value && !isKartaDescriptionValid.value) {
            try {
                //作成したかるたの固有uid作成
                val kartaUid = UUID.randomUUID().toString().replace("-", "")
                //作成したかるたの読み札・タイトル保存用のsharePref
                val sharedPref = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                //作成したかるたを保存するローカルディレクトリを作成
                val dir = File(context.filesDir, "karta/$kartaUid")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                for (index in kartaDataList.value.indices) {
                    val imageFile = File(dir, "$index.png")
                    val inputStream = context.contentResolver.openInputStream(kartaDataList.value[index].efuda.toUri())
                    val outputStream = FileOutputStream(imageFile)
                    //かるた絵札の書き込み
                    inputStream.use { input ->
                        outputStream.use { output ->
                            input?.copyTo(output)
                        }
                    }
                    //かるた読み札の保存
                    editor.putString(index.toString(), kartaDataList.value[index].yomifuda)
                }
                //かるたのタイトル・説明文保存
                editor.putString("title", kartaTitle.value)
                editor.putString("description", kartaDescription.value)
                editor.putString("uid", kartaUid)
                editor.putString("state", "ローカル")
                editor.apply()
                showInputTitleDialog.value = false
                Toast.makeText(context, "保存に成功しました", Toast.LENGTH_SHORT).show()
                //かるた一覧画面へ移動
                val intent = Intent(context, EFUDAActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            } catch (e: Exception) {
                Toast.makeText(context, "保存に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
    }
}