package com.example.sleephony

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.sleephony.navigation.AppNavGraph
import com.example.sleephony.service.WearMessageListener
import com.example.sleephony.ui.theme.SleephonyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.wearable.Wearable
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("KeyHash", "${Utility.getKeyHash(this)}")
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }
}

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

                // 내비게이션 관련
                val navController = rememberNavController()

                AppNavGraph(navController = navController)
            }
        }
    }
}
