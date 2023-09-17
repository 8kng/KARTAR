package com.example.myapplication.model.realTimeDatabase

data class RoomInfo @JvmOverloads constructor(
    var isStart: Boolean = false,
    var count: Int = 1,
    var kind: String = "public",
    var roomName: String = "ルーム"
)
