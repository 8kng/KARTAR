package com.example.myapplication.view.screen.create

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.HomeActivityScreen
import com.example.myapplication.HomeActivityScreenView
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow
import com.example.myapplication.theme.Yellow2
import com.example.myapplication.view.widget.AppBar

class EFUDAActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            EFUDAScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EFUDAScreen(profileViewModel: ProfileViewModel = ProfileViewModel(), createViewModel: CreateViewModel = CreateViewModel()) {
    Scaffold(
        topBar = { AppBar(profileViewModel) }
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
                RowButtons()
                Spacer(modifier = Modifier.height(30.dp))
                SearchBox(createViewModel = createViewModel)
            }
        }
    }
}

@Composable
private fun RowButtons() {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(20.dp))
        ButtonContent(onClick = {
            val intent = Intent(context, CreateMethodSelectActivity()::class.java)
            context.startActivity(intent)
                                },
            text = "作成"
        )
        Spacer(modifier = Modifier.width(30.dp))
        ButtonContent(onClick = { /*TODO*/ }, text = "サーバ検索")
    }
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
                fontWeight = FontWeight.Bold,
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ButtonContent(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {
    Surface(
        modifier = modifier.shadow(10.dp),
        onClick = { onClick() }
    ) {
        Box(
            modifier
                .border(width = 5.dp, color = LiteGreen)
                .height(70.dp)
                .background(Yellow)
                .width(145.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier,
                color = Grey,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
fun EFUDAScreenPreview() {
    EFUDAScreen()
}
