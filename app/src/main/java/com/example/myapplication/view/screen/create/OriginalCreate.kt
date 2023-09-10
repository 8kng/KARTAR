package com.example.myapplication.view.screen.create

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.ButtonBorder
import com.example.myapplication.theme.ButtonContainer
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.DarkRed
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow2
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent

class OriginalCreateActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            //OriginalCreateScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginalCreateScreen(navController: NavController, profileViewModel: ProfileViewModel = ProfileViewModel(LocalContext.current), createViewModel: CreateViewModel = CreateViewModel()) {
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
                KARTALazyRow(createViewModel = createViewModel)
                Spacer(modifier = Modifier.height(5.dp))
                if (createViewModel.isYomifudaValid.value) {
                    Text(text = "入力に問題があります", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(80.dp))
                SaveKartaButton(createViewModel)
            }
        }
    }
    if (createViewModel.showInputTitleDialog.value) {
        InputTitleDialog(createViewModel = createViewModel)
    }
}

@Composable
fun KARTALazyRow(createViewModel: CreateViewModel) {
    LazyRow(
        modifier = Modifier.padding(start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(createViewModel.kartaDataList.value.size) { index ->
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .padding(start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingText(text = "絵札")
                Spacer(modifier = Modifier.height(10.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .border(
                                width = 10.dp,
                                color = ButtonBorder,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .background(color = ButtonContainer)
                    ) {
                        EfudaImageSelect(createViewModel = createViewModel, index = index)
                    }
                    CircleText(
                        modifier = Modifier.align(Alignment.TopEnd),
                        createViewModel = createViewModel,
                        index = index,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                SettingText(text = "読み札")
                Spacer(modifier = Modifier.height(5.dp))
                YomifudaTextField(createViewModel = createViewModel, index = index)
            }
        }
    }
}

@Composable
private fun SettingText(text: String) {
    Text(
        text = text,
        color = DarkRed,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.kiwimaru_medium)),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CircleText(modifier: Modifier, createViewModel: CreateViewModel, index: Int) {
    Box(
        modifier = modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EfudaImageSelect(createViewModel: CreateViewModel, index: Int) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            createViewModel.onChangeEfuda(uri, index)
        }
    }
    Surface(
        onClick = { launcher.launch("image/*") }
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Grey2,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(Color.White, shape = RoundedCornerShape(5.dp))
                .width(200.dp)
                .height(280.dp)
        ) {
            if (createViewModel.kartaDataList.value[index].efuda == "") {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "画像を選ぶ",
                    color = Grey2
                )
            } else {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(280.dp),
                    painter = rememberAsyncImagePainter(createViewModel.kartaDataList.value[index].efuda.toUri()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YomifudaTextField(createViewModel: CreateViewModel, index: Int) {
    TextField(
        modifier = Modifier.height(50.dp),
        value = createViewModel.kartaDataList.value[index].yomifuda,
        onValueChange = { newValue ->
            createViewModel.onChangeYomifuda(newValue, index)
        },
        singleLine = true,
        isError = createViewModel.isYomifudaValid.value,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = DarkRed,
            unfocusedIndicatorColor = Grey2.copy(alpha = 0.6f),

        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.kiwimaru_regular)), fontSize = 16.sp)
    )
}

@Composable
private fun SaveKartaButton(createViewModel: CreateViewModel) {
    val context = LocalContext.current
    ButtonContent(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
            .background(Color.Transparent),
        onClick = { createViewModel.onClickSaveButton(context = context) },
        text = "保存する",
        border = 4,
        fontSize = 16,
    )
}

@Composable
fun InputTitleDialog(createViewModel: CreateViewModel) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { createViewModel.showInputTitleDialog.value = false },
        title = {
            Text(
                text = "かるたの登録",
                color = Grey,
                fontFamily = FontFamily(Font(R.font.kiwimaru_medium))
            )
                },
        text = {
               Column {
                   EditField(
                       value = createViewModel.kartaTitle.value,
                       onClick = { newValue -> createViewModel.onChangeKartaTitle(newValue) },
                       isError = createViewModel.isKartaTitleValid.value,
                       placeholder = "1～15文字で入力してください",
                       label = "かるたのタイトル"
                   )
                   Spacer(modifier = Modifier.height(10.dp))
                   EditField(
                       value = createViewModel.kartaDescription.value,
                       onClick = { newValue -> createViewModel.onChangeKartaDescription(newValue) },
                       isError = createViewModel.isKartaDescriptionValid.value,
                       placeholder = "1～20文字で入力してください",
                       label = "かるたの説明"
                   )
               }
        },
        confirmButton = {
            TextButton(
                onClick = { createViewModel.saveKartaToLocal(context = context) }
            ) {
                Text(
                    text = "OK",
                    color = DarkGreen,
                    fontSize = 16.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { createViewModel.showInputTitleDialog.value = false }
            ) {
                Text(
                    text = "NO",
                    color = Grey2,
                    fontSize = 16.sp
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditField(
    value: String,
    onClick: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean,
    label: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onClick(newValue)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 12.sp,
                color = Grey2
            )
        },
        isError = isError,
        label = {
            Text(
                text = label,
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Grey,
            focusedBorderColor = DarkGreen,
            unfocusedBorderColor = LiteGreen,
            containerColor = Yellow2
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
    )
}

@Preview
@Composable
private fun OriginalCreateScreenPreview() {
    //OriginalCreateScreen()
}