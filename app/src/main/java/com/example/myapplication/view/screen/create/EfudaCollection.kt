package com.example.myapplication.view.screen.create

import androidx.compose.foundation.layout.Arrangement
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
import com.example.myapplication.theme.ButtonContainer
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow2
import com.example.myapplication.view.widget.AppBar
import com.example.myapplication.view.widget.button.ButtonContent

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
