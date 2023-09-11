package com.example.myapplication.controller

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.KartaDataFromServer
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KartaSearchViewModel : ViewModel(){
    //検索ボックス用の変数
    val searchKeyword = mutableStateOf("")
    val searchBoxPlaceholder = mutableStateOf("1～21文字で入力してください")
    val searchBoxLabel = mutableStateOf("かるたの検索")
    private val searchType = mutableStateOf("normal")
    //かるたデータ取得用の変数
    val kartaDataFromServerList = mutableStateOf(
        listOf(KartaDataFromServer(kartaUid = "", create = "", title = "", description = "", genre = "", kartaImage = ""))
    )

    init {
        getAllKartaDataFromServer()
    }

    //検索方法の変化に対する処理
    fun changeSearchType() {
        //検索方法の変更
        when (searchType.value) {
            "normal" -> {
                searchType.value = "title"
                searchBoxPlaceholder.value = "1～15文字で入力してください"
                searchBoxLabel.value = "かるたのタイトルで検索"
            }
            "title" -> {
                searchType.value = "description"
                searchBoxPlaceholder.value = "1～21文字で入力してください"
                searchBoxLabel.value = "かるたの説明で検索"
            }
            "description" -> {
                searchType.value = "genre"
                searchBoxPlaceholder.value = "1～21文字で入力してください"
                searchBoxLabel.value = "かるたのジャンルで検索"
            }
            else -> {
                searchType.value = "normal"
                searchBoxPlaceholder.value = "1～21文字で入力してください"
                searchBoxLabel.value = "かるたの検索"
            }
        }
    }

    //検索ボックスの入力処理
    fun onClickServerKartaSearchBox(newValue: String) {
        when (searchType.value) {
            "title" -> {
                if (newValue.length < 16) {
                    searchKeyword.value = newValue
                }
            }
            else -> {
                if (newValue.length < 21) {
                    searchKeyword.value = newValue
                }
            }
        }
        getKartaDataFromServer()
    }

    //サーバからかるた情報を取得
    private fun getKartaDataFromServer() {
        viewModelScope.launch {
            val newKartaDataList = mutableListOf<KartaDataFromServer>()
            val firestore = FirebaseFirestore.getInstance()

            when (searchType.value) {
                "normal" -> {
                    val querySnapshot = firestore.collection("kartaes").get().await()

                    for (document in querySnapshot.documents) {
                        val title = document.getString("title") ?: ""
                        val description = document.getString("description") ?: ""
                        val genre = document.getString("genre") ?: ""

                        if (title.contains(searchKeyword.value) || description.contains(searchKeyword.value) || genre.contains(searchKeyword.value)) {
                            val efuda = firestore.collection("kartaes")
                                .document(document.id)
                                .collection("efuda")
                                .document("0")
                                .get()
                                .await()

                            newKartaDataList.add(KartaDataFromServer(
                                kartaUid = document.id,
                                create = document.get("create").toString(),
                                title = title,
                                description = description,
                                genre = genre,
                                kartaImage = efuda.get("efuda").toString()
                            ))
                        }
                    }

                    kartaDataFromServerList.value = newKartaDataList
                }
                "title" -> {  //タイトル検索時
                    val querySnapshot = firestore.collection("kartaes").get().await()

                    for (document in querySnapshot.documents) {
                        val title = document.getString("title") ?: ""

                        if (title.contains(searchKeyword.value)) {
                            val efuda = firestore.collection("kartaes")
                                .document(document.id)
                                .collection("efuda")
                                .document("0")
                                .get()
                                .await()

                            newKartaDataList.add(KartaDataFromServer(
                                kartaUid = document.id,
                                create = document.get("create").toString(),
                                title = title,
                                description = document.get("description").toString(),
                                genre = document.get("genre").toString(),
                                kartaImage = efuda.get("efuda").toString()
                            ))
                        }
                    }

                    kartaDataFromServerList.value = newKartaDataList
                }
                "description" -> {
                    val querySnapshot = firestore.collection("kartaes").get().await()

                    for (document in querySnapshot.documents) {
                        val description = document.getString("description") ?: ""

                        if (description.contains(searchKeyword.value)) {
                            val efuda = firestore.collection("kartaes")
                                .document(document.id)
                                .collection("efuda")
                                .document("0")
                                .get()
                                .await()

                            newKartaDataList.add(KartaDataFromServer(
                                kartaUid = document.id,
                                create = document.get("create").toString(),
                                title = document.get("title").toString(),
                                description = description,
                                genre = document.get("genre").toString(),
                                kartaImage = efuda.get("efuda").toString()
                            ))
                        }
                    }

                    kartaDataFromServerList.value = newKartaDataList
                }
                else -> {
                    val querySnapshot = firestore.collection("kartaes").get().await()

                    for (document in querySnapshot.documents) {
                        val genre = document.getString("genre") ?: ""

                        if (genre.contains(searchKeyword.value)) {
                            val efuda = firestore.collection("kartaes")
                                .document(document.id)
                                .collection("efuda")
                                .document("0")
                                .get()
                                .await()

                            newKartaDataList.add(KartaDataFromServer(
                                kartaUid = document.id,
                                create = document.get("create").toString(),
                                title = document.get("title").toString(),
                                description = document.get("description").toString(),
                                genre = genre,
                                kartaImage = efuda.get("efuda").toString()
                            ))
                        }
                    }

                    kartaDataFromServerList.value = newKartaDataList
                }
            }
        }
    }

    private fun getAllKartaDataFromServer() {
        val newKartaDataList = mutableListOf<KartaDataFromServer>()
        val firestore = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            val querySnapshot = firestore.collection("kartaes").get().await()

            for (document in querySnapshot.documents) {
                val efuda = firestore.collection("kartaes")
                    .document(document.id)
                    .collection("efuda")
                    .document("0")
                    .get()
                    .await()

                newKartaDataList.add(KartaDataFromServer(
                    kartaUid = document.id,
                    create = document.get("create").toString(),
                    title = document.get("title").toString(),
                    description = document.get("description").toString(),
                    genre = document.get("genre").toString(),
                    kartaImage = efuda.get("efuda").toString()
                ))
            }

            kartaDataFromServerList.value = newKartaDataList
            Log.d("newValue", kartaDataFromServerList.value.toString())
        }
    }

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
        continuation.invokeOnCancellation {
            this.isCanceled
        }
    }
}