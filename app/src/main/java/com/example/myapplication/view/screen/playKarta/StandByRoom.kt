package com.example.myapplication.view.screen.playKarta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.controller.RoomCreateViewModel
import com.example.myapplication.theme.DarkRed
import com.example.myapplication.theme.Grey
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.CircleUserIcon
import com.example.myapplication.view.widget.PlayerIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandByRoomScreen(
    navController: NavController,
    roomCreateViewModel: RoomCreateViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WaitingNumberText(roomCreateViewModel = roomCreateViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            PlayerColumn(roomCreateViewModel = roomCreateViewModel)
        }
    }
}

@Composable
fun WaitingNumberText(roomCreateViewModel: RoomCreateViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,) {
        Text(
            text = "待機人数",
            color = Grey,
            fontSize = 18.sp
        )
        Text(text = "${roomCreateViewModel.standByPlayer.value}/4")
    }
}

@Composable
fun PlayerColumn(roomCreateViewModel: RoomCreateViewModel) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .height(220.dp)
    ) {
        items(roomCreateViewModel.playerInformation.value.size) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkRed.copy(alpha = 0.05f), shape = RoundedCornerShape(5.dp))
            ) {
                Row {
                    PlayerIcon(
                        modifier = Modifier.size(30.dp),
                        borderWidth = 1,
                        model = roomCreateViewModel.playerInformation.value[index].second
                    )
                    Text(text = roomCreateViewModel.playerInformation.value[index].first)
                }
            }
        }
    }
}