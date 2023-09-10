package com.example.myapplication.view.widget

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.LiteGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController, profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    profileViewModel.updateUserIconFromSharedPref(context)
    TopAppBar(
        modifier = Modifier
            .height(70.dp)
            .padding(
                top = 14.dp,
                end = 20.dp
            ),
        title = { },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Toggle password visibility",
                )
            }
        },
        actions = {
            CircleUserIcon(
                modifier = Modifier.size(40.dp),
                profileViewModel = profileViewModel,
                borderWidth = 1,
                navController = navController
            )
        }
    )
}