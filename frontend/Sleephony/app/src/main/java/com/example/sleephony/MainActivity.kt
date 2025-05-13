package com.example.sleephony

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.sleephony.navigation.AppNavGraph
import com.example.sleephony.ui.theme.SleephonyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var initialRoute: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialRoute = intent.getStringExtra("start_destination") ?: "splash"

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SleephonyTheme {
                val sysUi = rememberSystemUiController()
                SideEffect {
                    // StatusBar 투명 + 아이콘 흰색(=darkIcons=false)
                    sysUi.setStatusBarColor(
                        color = Color(0xFF182741),
                        darkIcons = false
                    )
                    // NavigationBar 투명 + 아이콘 흰색
                    sysUi.setNavigationBarColor(
                        color = Color(0xFF182741),
                        darkIcons = false
                    )
                }

                // 내비게이션 관련
                val navController = rememberNavController()

                AppNavGraph(navController = navController)
            }
        }
    }
}
