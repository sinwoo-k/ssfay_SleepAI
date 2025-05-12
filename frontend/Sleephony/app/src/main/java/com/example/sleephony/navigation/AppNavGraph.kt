package com.example.sleephony.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.sleephony.ui.common.component.BottomNavBar
import com.example.sleephony.ui.screen.auth.ProfileSetupScreen
import com.example.sleephony.ui.screen.auth.ProfileViewModel
import com.example.sleephony.ui.screen.auth.SocialLoginScreen
import com.example.sleephony.ui.screen.report.ReportScreen
import com.example.sleephony.ui.screen.statistics.components.detail.SleepDetailScreen
import com.example.sleephony.ui.screen.settings.SettingsHomeScreen
import com.example.sleephony.ui.screen.sleep.SleepMeasurementScreen
import com.example.sleephony.ui.screen.sleep.SleepSettingScreen
import com.example.sleephony.ui.screen.sleep.SleepUiState
import com.example.sleephony.ui.screen.sleep.SleepViewModel
import com.example.sleephony.ui.screen.splash.SplashScreen
import com.example.sleephony.ui.screen.splash.SplashViewModel
import com.example.sleephony.ui.screen.statistics.StatisticsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splash"
) {
    // 현재 경로 가져오기
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    // 바텀바를 보여줄 라우트 목록
    val bottomRoutes = listOf(
        "sleep_setting",
        "report",
        "statistics",
        "settings"
    )
    val showBottomBar = currentRoute in bottomRoutes

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable("splash") {
                val splashVm: SplashViewModel = hiltViewModel()
                SplashScreen(
                    navController = navController,
                    viewModel = splashVm
                )
            }

            composable("login") {
                SocialLoginScreen(
                    onNeedsProfile = {
                        navController.navigate("profile_setup") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onLoginSuccess = {
                        navController.navigate("sleep_setting") {
                            popUpTo("login") { inclusive = true }
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
            // 수면 측정 관련
            composable("sleep_setting") { backStackEntry ->
                val vm: SleepViewModel = hiltViewModel(backStackEntry)
                SleepSettingScreen(
                    viewModel = vm,
                    onStart = {
                        vm.onStartClicked()
                        navController.navigate("sleep_measurement") {
                            popUpTo("sleep_setting") { inclusive = false }
                        }
                    }
                )
            }
            composable("sleep_measurement") {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val settingEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry("sleep_setting")
                }
                val vm: SleepViewModel = hiltViewModel(settingEntry)
                val uiState by vm.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    if(uiState is SleepUiState.Setting) {
                        navController.navigate("sleep_setting") {
                            popUpTo("sleep_measurement") { inclusive = true }
                        }
                    }
                }

                SleepMeasurementScreen(
                    onStop = {
                        vm.onStopClicked()
                        navController.navigate("sleep_setting") {
                            popUpTo("sleep_measurement") { inclusive = false }
                        }
                    },
                    viewModel = vm
                )
            }

            composable("report") {
                ReportScreen()
            }

            composable("statistics") {
                StatisticsScreen(modifier = Modifier, navController = navController)
            }

            composable("settings") {
                SettingsHomeScreen(
                    logout = {
                        navController.navigate("login") {
                            popUpTo("settings") { inclusive = true}
                        }
                    }
                )
            }

            composable("detail/{page}/{period}") { it ->
                val page = it.arguments?.getString("page") ?: ""
                val period = it.arguments?.getString("period") ?: ""
                val days = when (period) {
                    "week" -> listOf("월", "화", "수", "목", "금", "토", "일")
                    "month" -> listOf("1주", "2주", "3주", "4주", "5주")
                    "year" -> listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
                    else -> emptyList()
                }
                SleepDetailScreen(
                    page = page,
                    navController = navController,
                    days = days
                )
            }
        }
    }
}