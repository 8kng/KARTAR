package com.example.myapplication.view.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerIcon(
    modifier: Modifier,
    borderWidth: Int,
    model: String
) {
    AsyncImage(
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .border(
                width = borderWidth.dp,
                color = Grey,
                shape = CircleShape
            )
            .clip(CircleShape)
    )
}