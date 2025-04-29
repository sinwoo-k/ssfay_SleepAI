package com.example.sleephony

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.sleephony.ui.screen.splash.SplashScreen
import com.example.sleephony.ui.theme.SleephonyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SleephonyTheme {
                val sysUi = rememberSystemUiController()
                SideEffect {
                    // StatusBar 투명 + 아이콘 흰색(=darkIcons=false)
                    sysUi.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = false
                    )
                    // NavigationBar 투명 + 아이콘 흰색
                    sysUi.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = false
                    )
                }
                SplashScreen{}
            }
        }
    }
}

//@Composable
//fun AppNavHost(){
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "splash"){
//        composable("splash"){
//            SplashScreen {
//                navController
//            }
//        }
//    }
//}
