package com.example.myapplication.controller

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.model.EnterPlayer
import com.example.myapplication.model.realTimeDatabase.RoomInfo
import com.example.myapplication.model.realTimeDatabase.gameInfo
import com.example.myapplication.model.realTimeDatabase.owner
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import kotlin.random.Random

class RoomCreateViewModel(context: Context) : ViewModel() {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.UserInformation), Context.MODE_PRIVATE)
    val roomIsPublic = mutableStateOf(true)
    //部屋の名前
    val roomName = mutableStateOf("")
    //val roomPassword = mutableStateOf("public")
    private val roomUid = mutableStateOf("")
    val playKartaUid = mutableStateOf("")
    val isRoomNameValid = mutableStateOf(false)
    //val isRoomPasswordValid = mutableStateOf(false)
    val selectedOption = mutableIntStateOf(1)
    //保存されたかるた一覧
    val kartaDirectories = mutableStateOf<List<File>>(listOf())
    val playKartaTitle = mutableStateOf("")
    //入った部屋
    val enterRoom = mutableStateOf("")
    val standByPlayer = mutableStateOf(0)
    val allPlayers = mutableStateOf<List<EnterPlayer>>(listOf())
    val playerInformation = mutableStateOf<List<Pair<String, String>>>(listOf())

    init {
        roomUid.value = UUID.randomUUID().toString().replace("-", "")
        getKartaDir(context = context)
    }

    //かるたディレクトリから名前を取得
    private fun getKartaDir(context: Context) {
        val dir = File(context.filesDir, "karta")
        kartaDirectories.value = dir.listFiles()?.filter { it.isDirectory } ?: listOf()
    }

    //部屋の名前入力処理
    fun onRoomNameChange(newString: String) {
        if (newString.length < 16) {
            roomName.value = newString
        }
    }

    //部屋のパスワード入力処理
    fun onRoomPasswordChange(newString: String) {
        val regex = Regex("^[a-zA-Z0-9]*$")
        if (newString.length < 16 && regex.matches(newString)) {
            //roomPassword.value = newString
        }
    }

    //ゲーム部屋の作成
    fun gameRoomCreate(context: Context, navController: NavController) {
        val net = isNetworkAvailable(context)
        if (roomName.value != "" && playKartaUid.value != "" && net) {
            viewModelScope.launch {
                val database = FirebaseDatabase.getInstance()
                val createRoom = database.getReference("room").child(roomUid.value)
                //オーナ情報記入
                createRoom.child("owner")
                    .setValue(
                        owner(
                            uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        )
                    )
                //自身のポイント初期化
                createRoom.child("point")
                    .setValue(mapOf(FirebaseAuth.getInstance().currentUser?.uid to 0))
                //部屋の情報設定
                val kind = if (roomIsPublic.value) {
                    "public"
                } else {
                    "private"
                }
                createRoom.child("roomInfo")
                    .setValue(RoomInfo(
                        kind = kind,
                        roomName = roomName.value
                    ))
                //ゲームの設定
                createRoom.child("gameInfo").setValue(
                    gameInfo(
                        kartaUid = playKartaUid.value,
                        next = randomTimeBetween("0:40", "2:00"),
                        play = selectedOption.value
                    )
                )
                //使うかるたを保存
                val hiraganaList = listOf("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ")
                val selectedHiragana = hiraganaList.shuffled().take(selectedOption.value)
                val map = selectedHiragana.mapIndexed { index, hiragana -> index to hiragana }.toMap()
                val dataToSave = mutableMapOf<String, String>()
                map.forEach { (key, value) ->
                    dataToSave[key.toString()] = value
                }
                createRoom.child("gameInfo").child("hiragana").setValue(dataToSave)
                Toast.makeText(context, "成功しました", Toast.LENGTH_SHORT).show()
                enterRoom.value = roomUid.value
                roomInformation(navController)
            }
        } else {
            Toast.makeText(context, "失敗しました", Toast.LENGTH_SHORT).show()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //ゲーム部屋の状態確認
    fun roomInformation(navController: NavController) {
        val database = FirebaseDatabase.getInstance()
        val enterRoom = database.getReference("room").child(enterRoom.value)
        enterRoom.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //現在の参加人数取得
                val roomInfo = snapshot.child("roomInfo").getValue(RoomInfo::class.java)
                Log.d("へや", roomInfo.toString())
                if (roomInfo != null) {
                    standByPlayer.value = roomInfo.count
                    Log.d("へや", standByPlayer.value.toString())
                }
                //現在の参加者取得
                val currentPlayer = mutableListOf<EnterPlayer>()
                snapshot.child("player").children.forEach { playerSnapshot ->
                    currentPlayer.add(EnterPlayer(
                        uid = playerSnapshot.key.toString(),
                        state = playerSnapshot.value.toString()
                    ))
                }
                val owner = snapshot.child("owner").getValue(owner::class.java)
                currentPlayer.add(EnterPlayer(
                    uid = owner?.uid.toString(),
                    state = "ok"
                ))
                allPlayers.value = currentPlayer
                getPlayerProfile()
                navController.navigate("standByRoom")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getPlayerProfile() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        val tasks = mutableListOf<Task<DocumentSnapshot>>()

        if (currentUserUid != null) {
            val firestore = FirebaseFirestore.getInstance()
            for (player in allPlayers.value) {
                val task = firestore.collection("users").document(player.uid).get()
                tasks.add(task)
            }

            Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { snapshots ->
                val currentList = snapshots.map {
                    Pair(
                        it.get("userName").toString(),
                        it.get("iconImage").toString()
                    )
                }
                playerInformation.value = currentList
                Log.d("リスト", playerInformation.toString())
            }
        } else {
            playerInformation.value = emptyList()
        }
    }

    private fun randomTimeBetween(start: String, end: String): String {
        // 時間を秒単位に変換する関数
        fun timeToSeconds(time: String): Int {
            val (minutes, seconds) = time.split(":").map { it.toInt() }
            return minutes * 60 + seconds
        }

        val startSeconds = timeToSeconds(start)
        val endSeconds = timeToSeconds(end)

        // ランダムな秒数を取得
        val randomSeconds = Random.nextInt(startSeconds, endSeconds + 1)

        // 秒数を "分:秒" 形式に変換
        val minutes = randomSeconds / 60
        val seconds = randomSeconds % 60

        return String.format("%02d:%02d", minutes, seconds)
    }
}