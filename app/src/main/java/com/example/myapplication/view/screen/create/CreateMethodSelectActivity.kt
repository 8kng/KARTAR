package com.example.myapplication.view.screen.create

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.LargeButtonContent

class CreateMethodSelectActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            CreateMethodSelectScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMethodSelectScreen(profileViewModel: ProfileViewModel = ProfileViewModel()) {
    val context = LocalContext.current
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
                LargeButtonContent(onClick = {
                    val intent = Intent(context, OriginalCreateActivity()::class.java)
                    context.startActivity(intent)
                }, text = "オリジナル\n作成")
                Spacer(modifier = Modifier.height(80.dp))
                LargeButtonContent(onClick = { /*TODO*/ }, text = "AI作成")
            }
        }
    }
}

@Preview
@Composable
private fun CreateMethodSelectScreenPreview() {
    CreateMethodSelectScreen()
}