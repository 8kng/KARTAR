package com.example.myapplication.view.screen.create

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.ButtonBorder
import com.example.myapplication.theme.ButtonContainer
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.DarkRed
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.Grey3
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EfudaCollectionScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    createViewModel: CreateViewModel
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
                RowButtons(navController)
                Spacer(modifier = Modifier.height(30.dp))
                SearchBox(createViewModel = createViewModel)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "かるた一覧",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                KartaDirectoryList(createViewModel = createViewModel)
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
        CreateButton(navController = navController)
        Spacer(modifier = Modifier.width(30.dp))
        SaverSearchButton()
    }
}

@Composable
private fun CreateButton(navController: NavController) {
    ButtonContent(
        modifier = Modifier
            .height(75.dp)
            .width(145.dp),
        onClick = { navController.navigate("createMethodSelect") },
        text = "作成",
        fontSize = 18,
        border = 6
    )
}

@Composable
private fun SaverSearchButton() {
    ButtonContent(
        modifier = Modifier
            .height(75.dp)
            .width(145.dp),
        onClick = { /*TODO:さーば検索画面へ移動*/ },
        text = "サーバ検索",
        fontSize = 18,
        border = 6
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(createViewModel: CreateViewModel) {
    OutlinedTextField(
        modifier = Modifier
            .height(56.dp)
            .width(320.dp),
        value = createViewModel.searchBoxText.value,
        onValueChange = { newValue ->
            createViewModel.onSearchBoxChange(newValue)
        },
        singleLine = true,
        isError = createViewModel.isSearchBoxTextValid.value,
        label = {
            Text(
                text = "検索",
                color = DarkGreen,
                fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
                fontWeight = FontWeight.Bold,
            )
        },
        placeholder = {
            Text(
                text = "1～20文字で入力してください",
                fontSize = 12.sp,
                color = Grey2
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Grey,
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = LiteGreen,
            containerColor = ButtonContainer
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
    )
}

@Composable
fun KartaDirectoryList(createViewModel: CreateViewModel) {
    val context = LocalContext.current
    createViewModel.getKartaDirectories(context)
    
    LazyColumn {
        items(createViewModel.kartaDirectories.value.size) { index ->
            val sharedPref = context.getSharedPreferences(createViewModel.kartaDirectories.value[index].name, Context.MODE_PRIVATE)
            val kartaName = sharedPref.getString("title", "")
            val kartaDescription = sharedPref.getString("description", "")

            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .background(DarkRed.copy(alpha = 0.05f), shape = RoundedCornerShape(5.dp))
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    kartaImage(createViewModel, index)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = kartaName.toString(),
                            fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
                            fontSize = 18.sp,
                            color = Grey
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = kartaDescription.toString(),
                            fontSize = 14.sp,
                            color = Grey2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun kartaImage(createViewModel: CreateViewModel, index: Int) {
    val context = LocalContext.current
    val imageFile = File(context.filesDir, "karta/${createViewModel.kartaDirectories.value[index].name}/0.png")
    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
    Box {
        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = ButtonBorder,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(color = ButtonContainer.copy(alpha = 0.4f))
        ) {
            Image(
                modifier = Modifier
                    .height(98.dp)
                    .width(70.dp),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 3.dp)
                .size(30.dp)
                .background(Color.White, shape = CircleShape)
                .border(width = 3.dp, color = Grey, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "あ",
                fontSize = 14.sp,
                color = Grey,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.kiwimaru_regular))
            )
        }
    }
}
