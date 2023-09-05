package com.example.myapplication.view.screen.create

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.view.widget.AppBar

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
        if (createViewModel.kartaDataList.value != null) {
            items(createViewModel.kartaDataList.value!!) { item ->
                Text(text = item.yomifuda)
            }
        }
    }
}

@Preview
@Composable
fun OriginalCreateScreenPreview() {
    OriginalCreateScreen()
}