package com.example.myapplication.controller

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.EnterPlayer
import com.example.myapplication.model.KARTAData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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
    val enterRoomm = mutableStateOf("")
    val standByPlayer = mutableStateOf(0)
    val ownerUid = mutableStateOf("")
    val allPlayers = mutableStateOf<List<EnterPlayer>>(listOf())
    val playerInformation = mutableStateOf<List<Pair<String, String>>>(listOf())

    init {
        roomUid.value = UUID.randomUUID().toString().replace("-", "")
        getKartaDir(context = context)
    }

    //かるたディレクトリから名前を取得
    private fun getKartaDir(context: Context) {
        val dir = File(context.filesDir, "karta")
        kartaDirectories.value = dir.listFiles()
            ?.filter { it.isDirectory }
            ?.filter {
                val sharedPreferences = context.getSharedPreferences(it.nameWithoutExtension, Context.MODE_PRIVATE)
                val valuePref = sharedPreferences.getString("state", "サーバ")
                valuePref != "ローカル"
            }
            ?: listOf()
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
                enterRoomm.value = roomUid.value
                roomInformation(navController, context)
            }
        } else {
            Toast.makeText(context, "失敗しました", Toast.LENGTH_SHORT).show()
        }
    }

    fun exitRoom(navController: NavController) {
        navController.popBackStack("roomList", false)
        viewModelScope.launch {
            try {
                val database = FirebaseDatabase.getInstance()
                val createRoom = database.getReference("room").child(enterRoomm.value)
                if (ownerUid.value == FirebaseAuth.getInstance().currentUser?.uid) {
                    createRoom.setValue(null)
                } else {
                    createRoom.child("roomInfo").child("count").get()
                        .addOnSuccessListener { snapshot ->
                            val currentValue: Int? = snapshot.getValue(Int::class.java)
                            if (currentValue != null) {
                                createRoom.child("roomInfo").child("count").setValue(currentValue - 1)
                                //ポイント削除
                                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                createRoom.child("point").child(userId).setValue(null)
                                //プレーヤー削除
                                createRoom.child("player").child(userId).setValue(null)
                            }
                        }
                }
            } catch (e:Exception) {
                Log.d("エラー", e.message.toString())
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //ゲーム部屋の状態確認
    fun roomInformation(navController: NavController, context: Context) {
        val database = FirebaseDatabase.getInstance()
        val enterRoom = database.getReference("room").child(enterRoomm.value)
        navController.navigate("standByRoom")
        enterRoom.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    navController.navigate("roomList")
                }else {
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
                    //オーナ情報取得
                    val owner = snapshot.child("owner").getValue(owner::class.java)
                    currentPlayer.add(EnterPlayer(
                        uid = owner?.uid.toString(),
                        state = "ok"
                    ))
                    ownerUid.value = owner?.uid ?: ""
                    //かるた情報
                    val gameInfo = snapshot.child("gameInfo").child("kartaUid").getValue(String::class.java)
                    Log.d("stream", "分岐:${gameInfo}")
                    if (!File(context.filesDir, "karta/${gameInfo}").exists()) {
                        if (gameInfo != null) {
                            Log.d("stream", "かるたdownload")
                            val firestore = FirebaseFirestore.getInstance()
                            val currentList = mutableListOf<KARTAData>()
                            val kartaUid = gameInfo
                            var kartaTitle = ""
                            var kartaDescription = ""
                            var kartaGenre = ""
                            viewModelScope.launch {
                                val getKartaInfo = firestore.collection("kartaes").document(kartaUid).get()
                                    .addOnSuccessListener { snapshot ->
                                        kartaTitle = snapshot.get("title").toString()
                                        kartaDescription = snapshot.get("description").toString()
                                        kartaGenre = snapshot.get("genre").toString()
                                    }
                                for (index in 0 until 44) {
                                    val efudaTask = firestore.collection("kartaes").document(kartaUid)
                                        .collection("efuda").document(index.toString()).get().await()

                                    val yomifudaTask = firestore.collection("kartaes").document(kartaUid)
                                        .collection("yomifuda").document(index.toString()).get().await()

                                    currentList.add(KARTAData(
                                        efuda = efudaTask.get("efuda").toString(),
                                        yomifuda = yomifudaTask.get("yomifuda").toString()
                                    ))
                                }

                                if (currentList.size == 44) {
                                    Log.d("listtt", currentList.toString())
                                    try {
                                        val sharedPreferences = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        val dir = File(context.filesDir, "karta/$kartaUid")
                                        if (!dir.exists()) {
                                            dir.mkdirs()
                                        }
                                        editor.putString("state", "他人")
                                        editor.putString("title", kartaTitle)
                                        editor.putString("description", kartaDescription)
                                        editor.putString("genre", kartaGenre)
                                        editor.putString("uid", kartaUid)
                                        val jobs = mutableListOf<Job>()
                                        currentList.forEachIndexed { index, item ->
                                            editor.putString(index.toString(), item.yomifuda)
                                            val job = launch(Dispatchers.IO) {
                                                val bitmap = Glide.with(context).asBitmap().load(item.efuda).submit().get()
                                                val file = File(dir, "$index.png")
                                                FileOutputStream(file).use { stream ->
                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                                    Log.d("stream", "画像を保存した$index")
                                                }
                                            }
                                            jobs.add(job)
                                        }
                                        jobs.forEach { it.join() }
                                        editor.apply()
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "保存しました", Toast.LENGTH_SHORT).show()
                                            val database = FirebaseDatabase.getInstance()
                                            val userRef = database.getReference("room")
                                                .child(enterRoomm.value)
                                                .child("player")
                                                .child(FirebaseAuth.getInstance().uid.toString())

                                            userRef.setValue("ok")
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            Log.d("エラー", e.message.toString())
                                            Toast.makeText(context, "保存に失敗しました", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.getReference("room")
                            .child(enterRoomm.value)
                            .child("player")
                            .child(FirebaseAuth.getInstance().uid.toString())

                        userRef.setValue("ok")
                    }
                    allPlayers.value = currentPlayer
                    getPlayerProfile()
                }
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