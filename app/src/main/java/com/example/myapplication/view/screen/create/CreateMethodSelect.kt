package com.example.myapplication.view.screen.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMethodSelectScreen(navController: NavController, profileViewModel: ProfileViewModel) {
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
                Spacer(modifier = Modifier.height(80.dp))
                OriginalCreateButton(navController = navController)
                Spacer(modifier = Modifier.height(80.dp))
                AICreateButton()
            }
        }
    }
}

@Composable
private fun OriginalCreateButton(navController: NavController) {
    ButtonContent(
        modifier = Modifier
            .height(150.dp)
            .width(250.dp),
        onClick = { navController.navigate("originalCreate") },
        text = "オリジナル\n作成"
    )
}

@Composable
private fun AICreateButton() {
    ButtonContent(
        modifier = Modifier
            .height(150.dp)
            .width(250.dp),
        onClick = { /*TODO:AIかるた作成画面移動*/ },
        text = "AI作成"
    )
}

@Preview
@Composable
private fun CreateMethodSelectScreenPreview() {
    //CreateMethodSelectScreen()
}