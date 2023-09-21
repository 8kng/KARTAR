package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.KartaSearchViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.controller.RoomCreateViewModel
import com.example.myapplication.controller.RoomListViewModel
import com.example.myapplication.view.screen.HomeScreen
import com.example.myapplication.view.screen.auth.LoginScreen
import com.example.myapplication.view.screen.auth.NotLoggedInScreen
import com.example.myapplication.view.screen.auth.SignInScreen
import com.example.myapplication.view.screen.create.CreateMethodSelectScreen
import com.example.myapplication.view.screen.create.EfudaCollectionScreen
import com.example.myapplication.view.screen.create.KartaDetailScreen
import com.example.myapplication.view.screen.create.OriginalCreateScreen
import com.example.myapplication.view.screen.create.server.ServerEfudaCollectionScreen
import com.example.myapplication.view.screen.create.server.ServerKartaDetail
import com.example.myapplication.view.screen.playKarta.RoomCreateScreen
import com.example.myapplication.view.screen.playKarta.RoomListScreen
import com.example.myapplication.view.screen.playKarta.StandByRoomScreen
import com.example.myapplication.view.screen.playKarta.solo.SoloSetupScreen
import com.example.myapplication.view.screen.profile.ProfileSetupScreen
import com.example.myapplication.view.screen.profile.UserProfileScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAppScreen()
        }
    }

    @Composable
    fun MyAppScreen() {
        val navController = rememberNavController()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val sharedPref = getSharedPreferences("UserInformation", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("UserName", "")
        val context = LocalContext.current

        //最初の画面遷移先を指定する変数
        val startDestination: String =
            if (user != null) {
                if (userName == "") {  //ユーザ情報が未定の場合
                    "profileSetup"
                } else {  //ログイン・ユーザ情報が保存されている場合
                    "home"
            }
        } else {  //ログインできていない場合
            "notLoggedIn"
        }

        val kartaSearchViewModel = KartaSearchViewModel()
        val profileViewModel = ProfileViewModel(context)
        val roomList = RoomListViewModel(context)
        val createViewModel = CreateViewModel()
        val roomCreateViewModel = RoomCreateViewModel(context)

        NavHost(
            navController = navController,
            startDestination = startDestination,
            builder = {
                composable(Screen.Home.route) { HomeScreen(navController = navController, profileViewModel = profileViewModel) }
                //Auth関連のScreen
                composable(Screen.NotLoggedIn.route) { NotLoggedInScreen(navController = navController) }
                composable(Screen.Signin.route) { SignInScreen(navController, AuthViewModel()) }
                composable(Screen.Login.route) { LoginScreen(navController, authViewModel = AuthViewModel()) }
                //profile関連のScreen
                composable(Screen.ProfileSetup.route) { ProfileSetupScreen(profileViewModel = ProfileViewModel(context), navController) }
                composable(Screen.UserProfile.route) { UserProfileScreen(navController = navController, profileViewModel = profileViewModel) }
                //create関連のScreen
                composable(Screen.EfudaCollection.route) {
                    EfudaCollectionScreen(
                        navController,
                        profileViewModel = profileViewModel,
                        createViewModel = createViewModel
                    )
                }
                composable("${Screen.KartaDetail.route}/{kartaUid}") { navBackStackEntry ->
                    val kartaUid = navBackStackEntry.arguments?.getString("kartaUid").toString()
                    KartaDetailScreen(navController = navController, profileViewModel = profileViewModel, kartaUid = kartaUid, createViewModel = createViewModel)
                }
                composable(Screen.ServerEfudaCollection.route) { ServerEfudaCollectionScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    kartaSearchViewModel = kartaSearchViewModel
                ) }
                composable("${Screen.ServerKartaDetail.route}/{kartaUid}") {navBackStackEntry ->
                    val kartaUid = navBackStackEntry.arguments?.getString("kartaUid").toString()
                    ServerKartaDetail(
                        navController = navController,
                        profileViewModel = profileViewModel,
                        kartaSearchViewModel = kartaSearchViewModel,
                        kartaUid = kartaUid
                ) }
                composable(Screen.CreateMethodSelect.route) { CreateMethodSelectScreen(navController = navController ,profileViewModel = profileViewModel) }
                composable(Screen.OriginalCreate.route) { OriginalCreateScreen(navController = navController) }
                //room関連のscreen
                composable(Screen.RoomList.route) { RoomListScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomList,
                    roomCreateViewModel
                ) }
                //ソロプレイ用のScreen
                composable(Screen.SoloSetup.route) { SoloSetupScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomList
                ) }
                //ルーム作成用のScreen
                composable(Screen.RoomCreate.route) { RoomCreateScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomList,
                    roomCreateViewModel = roomCreateViewModel
                ) }
                //ルーム待機中
                composable(Screen.StandByRoom.route) { StandByRoomScreen(
                    navController = navController,
                    roomCreateViewModel = roomCreateViewModel
                ) }
            }
        )
    }

    sealed class Screen(val route: String) {
        object Home: Screen("home")
        object NotLoggedIn: Screen("notLoggedIn")
        object Signin: Screen("signin")
        object Login: Screen("login")
        object ProfileSetup: Screen("profileSetup")
        object EfudaCollection: Screen("efudaCollection")
        object KartaDetail: Screen("kartaDetail")
        object UserProfile: Screen("userProfile")
        object CreateMethodSelect: Screen("createMethodSelect")
        object OriginalCreate: Screen("originalCreate")
        object ServerEfudaCollection: Screen("serverEfudaCollection")
        object ServerKartaDetail: Screen("serverKartaDetail")
        object RoomList: Screen("roomList")
        object SoloSetup: Screen("soloSetup")
        object RoomCreate: Screen("roomCreate")
        object StandByRoom: Screen("standByRoom")
    }
}