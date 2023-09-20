package com.example.myapplication.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.MainActivity
import com.example.myapplication.model.KARTAData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreateViewModel : ViewModel() {
    val searchBoxText = mutableStateOf("")
    val isSearchBoxTextValid = mutableStateOf(false)
    //かるたを保存しているディレクトリー一覧
    val kartaDirectories = mutableStateOf<List<File>>(listOf())
    //オリジナルかるた作成用の変数
    val kartaDataList = mutableStateOf(listOf(KARTAData("", "")))
    val hiraganaList = listOf("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ")
    val isYomifudaValid = mutableStateOf(false)
    //タイトルと説明文を入力するダイアログ表示
    val showInputTitleDialog = mutableStateOf(false)
    val kartaTitle = mutableStateOf("")
    val kartaDescription = mutableStateOf("")
    val kartaGenre = mutableStateOf("")
    val isKartaTitleValid = mutableStateOf(false)
    val isKartaDescriptionValid = mutableStateOf(false)
    val isKartaGenreValid = mutableStateOf(false)
    //かるた削除のダイアログ
    val showKartaDeleteDialog = mutableStateOf(false)
    //インディケーター
    val showProcessIndicator = mutableStateOf(false)

    init {
        //かるたデータを初期化
        kartaDataList.value = hiraganaList.map { KARTAData(efuda = "", yomifuda = it) }
    }

    //検索ボックスの入力処理
    fun onSearchBoxChange(newValue: String) {
        if (newValue.length  < 21) {
            searchBoxText.value = newValue
        }
    }

    //かるたディレクトリー取得
    fun getKartaDirectories(context: Context) {
        val dir = File(context.filesDir, "karta")
        kartaDirectories.value = dir.listFiles()?.filter { it.isDirectory } ?: listOf()
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
        val currentList = kartaDataList.value.toMutableList()
        /*
        if (index in currentList.indices) {
            val updateItem = currentList[index].copy(efuda = newValue.toString())
            val updateList = currentList.toMutableList().apply {
                this[index] = updateItem
            }
            kartaDataList.value = updateList
        }
        * */
        for (i in 0 until 44) {
            if (i in currentList.indices) {
                val updateItem = currentList[i].copy(efuda = newValue.toString())
                currentList[i] = updateItem

            }
        }
        kartaDataList.value = currentList
    }

    //かるた保存ボタンを押した時の処理
    fun onClickSaveButton() {
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

    /*作成したかるたのジャンル文入力処理*/
    fun onChangeKartaGenre(newValue: String) {
        if (newValue.length < 21) {
            kartaGenre.value = newValue
        }
    }

    /*作成したかるたをローカルに保存する処理*/
    fun saveKartaToLocal(context: Context, navController: NavController) {
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
                editor.putString("genre", kartaGenre.value)
                editor.putString("uid", kartaUid)
                editor.putString("state", "ローカル")
                editor.apply()
                showInputTitleDialog.value = false
                Toast.makeText(context, "保存に成功しました", Toast.LENGTH_SHORT).show()
                navController.navigate("efudaCollection") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "保存に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //ローカルのかるたをサーバに送信する処理
    fun uploadKarta(context: Context, kartaUid: String) {
        val dir = File(context.filesDir, "karta/$kartaUid")
        val sharedPreferences = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
        val filesToUpload = dir.listFiles()?.filter { it.extension in listOf("jpg", "jpeg", "png") }
        val totalFiles = filesToUpload?.size ?: 0
        var successCount = 0
        //さーばにかるた保存
        showProcessIndicator.value = true
        try {
            //最初にかるたのタイトル・作者などの情報を追加
            if (FirebaseAuth.getInstance().currentUser?.uid != null) {
                FirebaseFirestore.getInstance()
                    .collection("kartaes")
                    .document(kartaUid)
                    .set(
                        hashMapOf(
                            "title" to sharedPreferences.getString("title", "かるたのタイトル"),
                            "description" to sharedPreferences.getString("description", "かるたの説明"),
                            "genre" to sharedPreferences.getString("genre", "かるたのジャンル"),
                            "create" to (FirebaseAuth.getInstance().currentUser?.uid ?: "KARTAR")
                        )
                    )
                filesToUpload?.forEach { file ->
                    if (file.extension in listOf("jpg", "jpeg", "png")) {
                        val fileUri = Uri.fromFile(file)
                        val storageRef = FirebaseStorage.getInstance().reference
                        val imageRef = storageRef.child("karta/${kartaUid}/${file.name}")
                        val uploadTask = imageRef.putFile(fileUri)
                        uploadTask.addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener {downloadUri ->
                                Log.d("fileName", "実行:${file.nameWithoutExtension}:count${successCount}")
                                //読み札の保存
                                FirebaseFirestore.getInstance()
                                    .collection("kartaes")
                                    .document(kartaUid)
                                    .collection("yomifuda")
                                    .document(file.nameWithoutExtension)
                                    .set(hashMapOf(
                                        "yomifuda" to sharedPreferences.getString(file.nameWithoutExtension, "よみふだ"))
                                    )
                                FirebaseFirestore.getInstance()
                                    .collection("kartaes")
                                    .document(kartaUid)
                                    .collection("efuda")
                                    .document(file.nameWithoutExtension)
                                    .set(
                                        hashMapOf("efuda" to downloadUri.toString())
                                    )
                                    .addOnSuccessListener {
                                        Log.d("fileName", "success:${file.name}:count${successCount}")
                                        successCount++
                                        if (successCount == totalFiles) {
                                            val editor = sharedPreferences.edit()
                                            editor.putString("state", "サーバ")
                                            editor.apply()
                                            Toast.makeText(context, "サーバに登録しました", Toast.LENGTH_SHORT).show()
                                            showProcessIndicator.value = false
                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(context, "サーバに登録失敗しました...", Toast.LENGTH_SHORT).show()
                                        showProcessIndicator.value = false
                                        Log.d("fileName", "失敗:${file.name}:count${successCount}")
                                        return@addOnFailureListener
                                    }
                            }
                        }.addOnFailureListener{
                            Toast.makeText(context, "サーバに登録失敗しました...", Toast.LENGTH_SHORT).show()
                            showProcessIndicator.value = false
                            return@addOnFailureListener
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("ミス", e.message.toString())
            Toast.makeText(context, "ミス", Toast.LENGTH_SHORT).show()
        }
    }

    //かるたを削除する処理
    fun kartaDelete(navController: NavController, kartaUid: String, context: Context) {
        //画面戦意
        navController.popBackStack()
        showKartaDeleteDialog.value = false
        //かるたの絵札削除
        val contextFilesDir = context.filesDir
        val directoryToDelete = File(contextFilesDir, "karta/${kartaUid}")
        deleteRecursively(directoryToDelete)
        //かるたの読み札削除
        val sharedPreferences = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        context.deleteSharedPreferences(kartaUid)
    }

    private fun deleteRecursively(file: File): Boolean {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                deleteRecursively(it)
            }
        }
        return file.delete()
    }

}