package com.example.sleephony.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.sleephony.ui.screen.auth.ProfileSetupScreen
import com.example.sleephony.ui.screen.auth.ProfileViewModel
import com.example.sleephony.ui.screen.auth.SocialLoginScreen
import com.example.sleephony.ui.screen.report.ReportScreen
import com.example.sleephony.ui.screen.report.components.detail.SleepDetailScreen
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
        startDestination = "report"
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
                onNeedsProfile = {
                    navController.navigate("profile_setup") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginSuccess = {
                    navController.navigate("sleep_setting"){
                        popUpTo("login"){inclusive = true}
                    }
                }
            )
        }

        navigation(
            startDestination = ProfileStep.NICKNAME.route,  // 첫 화면: 닉네임
            route = "profile_setup"
        ) {
            ProfileStep.entries.forEach { step ->
                composable(step.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("profile_setup")
                    }

                    val profileVm: ProfileViewModel = hiltViewModel(parentEntry)

                    ProfileSetupScreen(
                        step = step,
                        viewModel = profileVm,
                        onNext = {
                            ProfileStep.entries.getOrNull(step.ordinal + 1)?.let { next ->
                                navController.navigate(next.route)
                            }
                            // 마지막 단계: ViewModel 에 제출 요청
                                ?: profileVm.submitProfile()
                        },
                        onComplete = {
                            navController.navigate("sleep_setting") {
                                popUpTo("profile_setup") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        composable("sleep_setting"){
            SleepSettingScreen()
        }

        composable("report") {
            ReportScreen(modifier = Modifier, navController = navController)
        }

        composable("detail/{page}") { it ->
            val page = it.arguments?.getString("page") ?: ""
            SleepDetailScreen(modifier = Modifier,page = page, navController = navController)
        }
    }
}
