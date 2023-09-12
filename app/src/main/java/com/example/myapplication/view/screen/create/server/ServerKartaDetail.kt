package com.example.myapplication.view.screen.create.server

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.controller.KartaSearchViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.view.widget.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerKartaDetail(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    kartaSearchViewModel: KartaSearchViewModel,
    kartaUid: String
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
                KartaInformationText(kartaSearchViewModel = kartaSearchViewModel, kartaUid = kartaUid)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun KartaInformationText(kartaSearchViewModel: KartaSearchViewModel, kartaUid: String) {
    kartaSearchViewModel.getKartaInformation(kartaUid)
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = kartaSearchViewModel.kartaTitle.value,
            fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
            fontSize = 18.sp,
            color = Grey
        )
        Text(
            text = kartaSearchViewModel.kartaGenre.value,
            fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
            fontSize = 16.sp,
            color = Grey2
        )
        Text(
            text = kartaSearchViewModel.kartaDescription.value,
            fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
            fontSize = 16.sp,
            color = Grey2
        )
    }
}