package com.example.myapplication.view.screen.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow
import com.example.myapplication.view.widget.button.OnValidCheckButton
import com.example.myapplication.view.widget.textField.UserNameTextField

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            UserProfileScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileScreen() {
    val context = LocalContext.current
    val profileViewModel = ProfileViewModel()
    Scaffold(
        topBar = { TopAppBar(
            title = {
                    Text(
                        text = "プロフィール",
                        modifier = Modifier.padding(start = 20.dp)
                    )
            },
            navigationIcon = {
                IconButton(
                    onClick = { (context as Activity).finish() },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = "Toggle password visibility",
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                navigationIconContentColor = Grey2,
                titleContentColor = Grey
            )
        ) }
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
                UserIconImage()
                Spacer(modifier = Modifier.height(50.dp))
                UserNameText(profileViewModel)
                Spacer(modifier = Modifier.height(120.dp))
                SignOutButton(profileViewModel = profileViewModel)
            }
            if (profileViewModel.showUserNameDialog.value) {
                UserNameChangeDialog(profileViewModel = profileViewModel)
            } else if(profileViewModel.showSignOutDialog.value) {
                SignOutDialog(profileViewModel = profileViewModel)
            }
        }
    }
}

@Composable
private fun UserIconImage() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.UserInformation),
        Context.MODE_PRIVATE
    )
    val imageUrl = sharedPref.getString(context.getString(R.string.imageIcon), "")
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = LiteGreen,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .size(170.dp)
                .align(Alignment.Center)
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 60.dp)
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.Cached,
                contentDescription = "Toggle password visibility",
                // 色はお好みで設定してください
                tint = DarkGreen,
            )
        }
    }
}

@Composable
private fun UserNameText(profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    profileViewModel.updateUserNameFromSharedPref(context)

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = profileViewModel.prefUserName.value,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center),
                color = Grey
            )
            Divider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp),
                color = Grey2
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { profileViewModel.showUserNameDialog.value = true },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = DarkGreen,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutButton(profileViewModel: ProfileViewModel) {
    Surface(
        modifier = Modifier.shadow(10.dp),
        onClick = {
            profileViewModel.showSignOutDialog.value = true
        }
    ) {
        Box(
            Modifier
                .border(width = 4.dp, color = LiteGreen)
                .height(46.dp)
                .background(Yellow)
                .width(260.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ログアウトする",
                modifier = Modifier,
                color = DarkGreen,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun UserNameChangeDialog(profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    Dialog(onDismissRequest = { profileViewModel.showUserNameDialog.value = true }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "新しい名前を入力してください",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                UserNameTextField(profileViewModel = profileViewModel)
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    profileViewModel.onClickUserNameDialog(context = context)
                }) {
                    Text(text = "OK", color = LiteGreen, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun SignOutDialog(profileViewModel: ProfileViewModel, authViewModel: AuthViewModel = AuthViewModel()) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { profileViewModel.showSignOutDialog.value = true },
        title = { Text(text = "最終確認", color = DarkGreen)},
        text = {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "ログアウトしてもよろしいですか？", color = Grey)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                profileViewModel.showSignOutDialog.value = false
            }) {
                Text(text = "NO", color = Grey2, fontSize = 16.sp)
            }
            TextButton(
                onClick = { authViewModel.signOutUser((context as Activity))}
            ) {
                Text(text = "OK", color = DarkGreen, fontSize = 16.sp)
            }
        }
    )
}

@Preview
@Composable
private fun UserProfileScreenPreview() {
    UserProfileScreen()
}