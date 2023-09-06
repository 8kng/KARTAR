package com.example.myapplication.view.screen.create

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.DarkRed
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.LargeButtonContent

class OriginalCreateActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            OriginalCreateScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginalCreateScreen(profileViewModel: ProfileViewModel = ProfileViewModel(), createViewModel: CreateViewModel = CreateViewModel()) {
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
                Spacer(modifier = Modifier.height(80.dp))
                KARTALazyRow(createViewModel = createViewModel)

                SaveKartaButton()
            }
        }
    }
}

@Composable
fun KARTALazyRow(createViewModel: CreateViewModel) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        items(createViewModel.kartaDataList.value.size) { index ->
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .height(300.dp)
                    .padding(start = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "読み札", color = DarkRed, fontSize = 16.sp)
                YomifudaTextField(createViewModel = createViewModel, index = index)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YomifudaTextField(createViewModel: CreateViewModel, index: Int) {
    TextField(
        value = createViewModel.kartaDataList.value[index].yomifuda,
        onValueChange = { newValue ->
            createViewModel.onChangeYomifuda(newValue, index)
        },
        singleLine = true,
    )
}

@Composable
private fun SaveKartaButton() {
    LargeButtonContent(
        modifier = Modifier
            .height(60.dp)
            .width(180.dp),
        onClick = { /*TODO*/ },
        text = "保存する",
        border = 5,
        fontWeight = FontWeight.Medium,
        fontSize = 20
    )
}

@Preview
@Composable
private fun OriginalCreateScreenPreview() {
    OriginalCreateScreen()
}