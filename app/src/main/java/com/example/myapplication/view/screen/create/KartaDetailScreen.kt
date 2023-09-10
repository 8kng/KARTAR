package com.example.myapplication.view.screen.create

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.ButtonBorder
import com.example.myapplication.theme.ButtonContainer
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KartaDetailScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    createViewModel: CreateViewModel,
    kartaUid: String
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
    val kartaState = sharedPref.getString("state", "").toString()
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
                KartaTitleText(kartaUid = kartaUid)
                KartaDescriptionText(kartaUid = kartaUid)
                Spacer(modifier = Modifier.height(20.dp))
                KartaDetailRow(kartaUid = kartaUid, createViewModel = createViewModel)
                Spacer(modifier = Modifier.height(50.dp))
                if (kartaState == "サーバ") {
                    SaverPopButton(createViewModel = createViewModel, kartaUid = kartaUid)
                }
            }
        }
    }
    //サーバ処理中に表示するインディケーター
    if (createViewModel.showProcessIndicator.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = LiteGreen)
        }
    }
}

@Composable
fun KartaTitleText(kartaUid: String) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
    val kartaName = sharedPref.getString("title", "かるたのタイトル").toString()
    Text(
        text = kartaName,
        fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
        fontSize = 18.sp,
        color = Grey
    )
}

@Composable
fun KartaDescriptionText(kartaUid: String) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
    val kartaDescription = sharedPref.getString("description", "").toString()
    Text(
        text = kartaDescription,
        fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
        fontSize = 16.sp,
        color = Grey2
    )
}

@Composable
fun KartaDetailRow(kartaUid: String, createViewModel: CreateViewModel) {
    val context = LocalContext.current
    val dir = File(context.filesDir, "karta/$kartaUid")
    val sharedPref = context.getSharedPreferences(kartaUid, Context.MODE_PRIVATE)
    //画像フォルダをすべて取得
    val allFiles = dir.listFiles()
    val imageFiles = allFiles.filter {
        it.isFile && (it.name.endsWith(".png") || it.name.endsWith(".jpg") || it.name.endsWith(".jpeg"))
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(44) { index ->
            val image = imageFiles[index].absoluteFile
            val yomifuda = sharedPref.getString(index.toString(), "").toString()
            Column(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingText(text = "絵札")
                KartaImage(imageFile = image, createViewModel = createViewModel, index = index)
                Spacer(modifier = Modifier.height(20.dp))
                SettingText(text = "読み札")
                Text(
                    text = yomifuda,
                    fontFamily = FontFamily(Font(R.font.kiwimaru_regular)),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun KartaImage(imageFile: File, createViewModel: CreateViewModel, index: Int) {
    val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
    val imagePainter = rememberAsyncImagePainter(model = imageBitmap)
    Box {
        Box(
            modifier = Modifier
                .border(
                    width = 8.dp,
                    color = ButtonBorder,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(color = ButtonContainer.copy(alpha = 0.4f))
        ) {
            Image(
                modifier = Modifier
                    .height(253.dp)
                    .width(180.dp),
                painter = imagePainter,
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 3.dp)
                .size(60.dp)
                .background(Color.White, shape = CircleShape)
                .border(width = 3.dp, color = Grey, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = createViewModel.hiraganaList[index],
                fontSize = 28.sp,
                color = Grey,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.kiwimaru_regular))
            )
        }
    }
}

@Composable
private fun SaverPopButton(createViewModel: CreateViewModel, kartaUid: String) {
    val context = LocalContext.current
    ButtonContent(
        modifier = Modifier
            .height(50.dp)
            .width(250.dp),
        onClick = { createViewModel.uploadKarta(context = context, kartaUid) },
        text = "みんなに公開",
        border = 4,
        fontSize = 18,
        fontWeight = FontWeight.Normal
    )
}

@Preview
@Composable
private fun KartaDetailScreenView() {
    KartaDetailScreen(
        navController = rememberNavController(),
        profileViewModel = ProfileViewModel(LocalContext.current),
        kartaUid = "",
        createViewModel = CreateViewModel()
    )
}