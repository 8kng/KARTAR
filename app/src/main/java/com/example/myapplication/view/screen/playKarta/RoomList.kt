package com.example.myapplication.view.screen.playKarta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.controller.RoomListViewModel
import com.example.myapplication.view.screen.create.EditField
import com.example.myapplication.view.screen.create.SearchBox
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    roomListViewModel: RoomListViewModel
) {
    Scaffold(
        topBar = { AppBar(navController, profileViewModel) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RowButtons(navController = navController)
                Spacer(modifier = Modifier.height(30.dp))
                RoomSearchBox(roomListViewModel = roomListViewModel)
                Spacer(modifier = Modifier.height(60.dp))
                SoloPlayButton(navController = navController)
            }
        }
    }
}

@Composable
private fun RowButtons(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(20.dp))
        RoomCreateButton(navController = navController)
        Spacer(modifier = Modifier.width(30.dp))
        RandomEnterRoomButton()
    }
}

@Composable
private fun RoomCreateButton(navController: NavController) {
    ButtonContent(
        modifier = Modifier
            .height(75.dp)
            .width(145.dp),
        onClick = {  },
        text = "部屋作成",
        fontSize = 18,
        border = 6
    )
}

@Composable
private fun RandomEnterRoomButton() {
    ButtonContent(
        modifier = Modifier
            .height(75.dp)
            .width(145.dp),
        onClick = { /*TODO:部屋作成画面へ遷移*/ },
        text = "ランダム\n入出",
        fontSize = 18,
        border = 6
    )
}

@Composable
fun RoomSearchBox(roomListViewModel: RoomListViewModel) {
    EditField(
        value = roomListViewModel.searchKeyword.value,
        placeholder = "1～20文字で入力してください",
        onClick = { newValue -> roomListViewModel.onSearchBoxChange(newValue) },
        isError = false,
        label = "部屋の検索"
    )
}

@Composable
fun SoloPlayButton(navController: NavController) {
    ButtonContent(
        modifier = Modifier
            .height(50.dp)
            .width(240.dp),
        onClick = { navController.navigate("soloSetup") },
        text = "ひとりで遊ぶ",
        fontSize = 18,
        border = 6
    )
}

@Preview
@Composable
fun RoomListPreview() {
    RoomListScreen(
        navController = rememberNavController(),
        profileViewModel = ProfileViewModel(LocalContext.current),
        roomListViewModel = RoomListViewModel()
    )
}