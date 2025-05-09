package com.example.sleephony.ui.screen.sleep

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.MainActivity
import com.example.sleephony.R
import com.example.sleephony.service.AlarmForegroundService
import com.example.sleephony.service.SleepMeasurementService
import com.example.sleephony.ui.common.animation.ShootingStar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.roundToInt

// AlarmActivity.kt
class AlarmActivity : ComponentActivity() {
    @SuppressLint("ContextCastToActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
                .requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        setContent {
            // Activity 레퍼런스
            val activity = LocalContext.current as ComponentActivity

            // 화면의 Y offset 상태
            val offsetY = remember { mutableFloatStateOf(0f) }
            // 드래그가 끝났을 때 애니메이션으로 돌아올 수 있게
            val animatedOffset by animateFloatAsState(
                targetValue = offsetY.floatValue,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )

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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF182741),
                                Color(0xFF314282),
                                Color(0xFF1D1437)
                            )
                        )
                    )
            )  {
                ShootingStar(
                    modifier = Modifier.fillMaxSize(),
                    delayMillis = 200,
                    durationMillis = 1000,
                    startXFrac = 0.1f, startYFrac = 0.2f,
                    endXFrac   = 0.5f, endYFrac   = 0.6f
                )
                ShootingStar(
                    modifier = Modifier.fillMaxSize(),
                    delayMillis = 1500,
                    durationMillis = 1000,
                    startXFrac = 0.2f, startYFrac = 0.15f,
                    endXFrac   = 0.9f, endYFrac   = 0.7f
                )
                ShootingStar(
                    modifier = Modifier.fillMaxSize(),
                    delayMillis = 1000,
                    durationMillis = 1000,
                    startXFrac = 0.4f, startYFrac = 0.05f,
                    endXFrac   = 0.9f, endYFrac   = 0.4f
                )
                Image(
                    painter = painterResource(R.drawable.bg_stars),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillWidth
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // 드래그한 만큼 화면을 함께 이동
                        .offset { IntOffset(0, animatedOffset.roundToInt()) }
                        // 세로 드래그 제스처 감지
                        .pointerInput(Unit) {
                            detectVerticalDragGestures (
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetY.floatValue = (offsetY.floatValue + dragAmount).coerceAtMost(0f)
                                },
                                onDragEnd = {
                                    // 위로 충분히 당겼으면 종료, 아니면 원위치로 돌아가기
                                    val threshold = size.height * 0.25f
                                    if (offsetY.floatValue < -threshold) {
                                        val navIntent = Intent(this@AlarmActivity, MainActivity::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            putExtra("start_destination", "sleep_setting")
                                        }
                                        stopService(Intent(this@AlarmActivity, AlarmForegroundService::class.java))

                                        stopService(Intent(this@AlarmActivity, SleepMeasurementService::class.java))

                                        sendBroadcast(Intent(SleepViewModel.ACTION_STOP_MEASUREMENT))

                                        startActivity(navIntent)
                                        activity.finish()
                                    } else {
                                        offsetY.floatValue = 0f
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_wakeup_phony),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Text(
                            text = "일어날 시간이에요!",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Spacer(Modifier.height(48.dp))

                        Text(
                            text = "밀어서 알람 해제",
                            color = Color(0xFF6DACF0),
                            fontSize = 32.sp
                        )
                    }
                }
            }
        }
    }
}
