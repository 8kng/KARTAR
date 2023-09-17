package com.example.myapplication.model

data class RoomData @JvmOverloads constructor(
    var name: String = "ルーム",
    var count: Int = 1,
    var isStart: Boolean = false,
    var kind: String = "public"
)
