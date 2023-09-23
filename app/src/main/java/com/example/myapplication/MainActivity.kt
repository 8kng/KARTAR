package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.controller.AuthViewModel
import com.example.myapplication.controller.CreateViewModel
import com.example.myapplication.controller.KartaSearchViewModel
import com.example.myapplication.controller.MainViewModel
import com.example.myapplication.controller.ProfileViewModel
import com.example.myapplication.controller.RoomCreateViewModel
import com.example.myapplication.controller.RoomListViewModel
import com.example.myapplication.controller.viewModelFactory.MainViewModelFactory
import com.example.myapplication.controller.viewModelFactory.ProfileViewModelFactory
import com.example.myapplication.controller.viewModelFactory.RoomCreateViewModelFactory
import com.example.myapplication.controller.viewModelFactory.RoomListViewModelFactory
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
        val context = LocalContext.current
        /*viewModelの作成*/
        val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
        val kartaSearchViewModel: KartaSearchViewModel = viewModel()
        val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))
        val roomListViewModel: RoomListViewModel = viewModel(factory = RoomListViewModelFactory(context))
        val createViewModel: CreateViewModel = viewModel()
        val roomCreateViewModel: RoomCreateViewModel = viewModel(factory = RoomCreateViewModelFactory(context))
        val authViewModel: AuthViewModel = viewModel()
        /*最初の画面遷移先を指定*/
        val startDestination: String = mainViewModel.getStartDestination()

        NavHost(
            navController = navController,
            startDestination = startDestination,
            builder = {
                /*ホーム画面*/
                composable(Screen.Home.route) { HomeScreen(navController = navController, profileViewModel = profileViewModel) }
                /**Auth**/
                /*未ログイン時の画面*/
                composable(Screen.NotLoggedIn.route) { NotLoggedInScreen(navController = navController) }
                /*サインイン画面*/
                composable(Screen.Signin.route) { SignInScreen(navController, authViewModel) }
                /*ログイン画面*/
                composable(Screen.Login.route) { LoginScreen(navController, authViewModel = authViewModel) }
                /*******
                 create
                 *********/
                /*かるた作成方法の選択画面*/
                composable(Screen.CreateMethodSelect.route) { CreateMethodSelectScreen(navController = navController ,profileViewModel = profileViewModel) }
                /*かるた一覧画面*/
                composable(Screen.EfudaCollection.route) {
                    EfudaCollectionScreen(
                        navController,
                        profileViewModel = profileViewModel,
                        createViewModel = createViewModel
                    )
                }
                /*かるたの詳細画面*/
                composable("${Screen.KartaDetail.route}/{kartaUid}") { navBackStackEntry ->
                    val kartaUid = navBackStackEntry.arguments?.getString("kartaUid").toString()
                    KartaDetailScreen(navController = navController, profileViewModel = profileViewModel, kartaUid = kartaUid, createViewModel = createViewModel)
                }
                /*自作かるたがめん*/
                composable(Screen.OriginalCreate.route) { OriginalCreateScreen(navController = navController) }
                /***************
                 create>>server
                 ***************/
                /*サーバかるた一覧*/
                composable(Screen.ServerEfudaCollection.route) { ServerEfudaCollectionScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    kartaSearchViewModel = kartaSearchViewModel
                ) }
                /*さーばかるたの詳細*/
                composable("${Screen.ServerKartaDetail.route}/{kartaUid}") {navBackStackEntry ->
                    val kartaUid = navBackStackEntry.arguments?.getString("kartaUid").toString()
                    ServerKartaDetail(
                        navController = navController,
                        profileViewModel = profileViewModel,
                        kartaSearchViewModel = kartaSearchViewModel,
                        kartaUid = kartaUid
                    ) }
                /**profile**/
                /*プロフィールの初期設定*/
                composable(Screen.ProfileSetup.route) { ProfileSetupScreen(profileViewModel = ProfileViewModel(context), navController) }
                /*プロフィール確認画面*/
                composable(Screen.UserProfile.route) { UserProfileScreen(navController = navController, profileViewModel = profileViewModel) }
                /**playKarta**/
                /*部屋一覧表示*/
                composable(Screen.RoomList.route) { RoomListScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomListViewModel,
                    roomCreateViewModel
                ) }
                /*ルーム作成*/
                composable(Screen.RoomCreate.route) { RoomCreateScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomListViewModel,
                    roomCreateViewModel = roomCreateViewModel
                ) }
                /*ルーム待機*/
                composable(Screen.StandByRoom.route) { StandByRoomScreen(
                    navController = navController,
                    roomCreateViewModel = roomCreateViewModel
                ) }
                /**playKarta>>solo**/
                //ソロプレイ用のScreen
                composable(Screen.SoloSetup.route) { SoloSetupScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    roomListViewModel = roomListViewModel
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