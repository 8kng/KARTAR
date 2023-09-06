package com.example.myapplication.view.widget.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeButtonContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    border: Int = 8,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: Int = 30
) {
    Surface(
        modifier = modifier.shadow(10.dp),
        onClick = { onClick() }
    ) {
        Box(
            modifier
                .border(width = border.dp, color = LiteGreen)
                .height(140.dp)
                .background(Yellow)
                .width(245.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Grey,
                fontSize = fontSize.sp,
                fontWeight = fontWeight
            )
        }
    }
}