package com.example.sleephony.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sleephony.ui.screen.auth.SocialLoginScreen
import com.example.sleephony.ui.screen.sleep.SleepSettingScreen
import com.example.sleephony.ui.screen.splash.SplashScreen
import com.example.sleephony.ui.screen.splash.SplashViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splash"
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable("splash"){
            val splashVm: SplashViewModel = hiltViewModel()
            SplashScreen(
                navController = navController,
                viewModel = splashVm
            )
        }

        composable("login"){
            SocialLoginScreen(
                onLoginSuccess = {
                    navController.navigate("sleep_setting"){
                        popUpTo("sleep_setting"){inclusive = true}
                    }
                }
            )
        }

        composable("sleep_setting"){
            SleepSettingScreen()
        }

    }
}
