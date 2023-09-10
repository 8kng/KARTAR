package com.example.myapplication.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.view.widget.CircleUserIcon
import com.example.myapplication.view.widget.button.ButtonContent

@Composable
fun HomeScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            CircleUserIcon(
                modifier = Modifier.size(80.dp),
                profileViewModel = profileViewModel,
                borderWidth = 3,
                navController = navController
            )
            Spacer(modifier = Modifier.height(80.dp))
            PlayKartaButton()
            Spacer(modifier = Modifier.height(70.dp))
            EfudaButton(navController)
        }
    }
}

@Composable
fun PlayKartaButton() {
    ButtonContent(
        onClick = { /*TODO:かるたで遊ぶ画面移動*/ },
        text = "かるたで\nあそぶ",
        modifier = Modifier
            .height(150.dp)
            .width(250.dp),
        fontSize = 34
    )
}

@Composable
fun EfudaButton(navController: NavController) {
    ButtonContent(
        onClick = { navController.navigate("efudaCollection") },
        text = "絵札",
        modifier = Modifier
            .height(150.dp)
            .width(250.dp),
        fontSize = 34
    )
}