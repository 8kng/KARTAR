package com.example.myapplication.view.screen.profile

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.theme.DarkGreen
import com.example.myapplication.theme.Grey
import com.example.myapplication.theme.Grey2
import com.example.myapplication.theme.LiteGreen
import com.example.myapplication.theme.Yellow2
import com.example.myapplication.view.widget.button.OnValidCheckButton

class ProfileSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val profileViewModel = ProfileViewModel()
        setContent {
            ProfileSetupActivityScreen(profileViewModel)
        }
    }
}

@Composable
fun ProfileSetupActivityScreen(profileViewModel: ProfileViewModel) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "登録ありがとうございます!")
                Text(text = "次に自身のプロフィールを入力してください")
                Spacer(modifier = Modifier.height(50.dp))
                UserIconImage(profileViewModel = profileViewModel)
                Spacer(modifier = Modifier.height(20.dp))
                UserNameTextField(profileViewModel = profileViewModel)
                Spacer(modifier = Modifier.height(140.dp))
                OnValidCheckButton { profileViewModel.onValidCheck(context) }
            }
            //サーバ処理中に表示
            if (profileViewModel.showProcessIndicator.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = LiteGreen)
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileSetupActivityScreenPreview() {
    ProfileSetupActivityScreen(ProfileViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameTextField(profileViewModel: ProfileViewModel) {
    OutlinedTextField(
        value = profileViewModel.userName.value,
        onValueChange = { newValue ->
            if (newValue.length  < 17) {
                profileViewModel.onUserNameChanged(newValue)
            }
        },
        singleLine = true,
        placeholder = {
            Text(
                text = "1～16文字で入力してください",
                fontSize = 12.sp,
                color = Grey2
            )
        },
        isError = profileViewModel.isUserNameValid.value,
        label = {
            Text(
                text = "ユーザネーム",
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

@Composable
fun UserIconImage(profileViewModel: ProfileViewModel) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profileViewModel.imageUri.value = uri
        }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        if (profileViewModel.imageUri.value != null) {
            Image(
                painter = rememberAsyncImagePainter(profileViewModel.imageUri.value),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = profileViewModel.circleColor.value,
                        shape = CircleShape
                    )
                    .size(170.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = profileViewModel.circleColor.value,
                        shape = CircleShape
                    )
                    .size(170.dp)
                    .clip(CircleShape)
            )
        }
        TextButton(
            onClick = { launcher.launch("image/*") }
        ) {
            Text(
                text = "アイコンをえらぶ",
                color = LiteGreen,
                fontSize = 16.sp,
            )
        }
    }
}