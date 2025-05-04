package com.example.sleephony.ui.screen.splash

import ShootingStar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sleephony.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = null)

    LaunchedEffect(isLoggedIn) {
        // 초기 null일 때도 잠깐 띄우고, boolean으로 바뀌면 바로 이동
        if (isLoggedIn != null) {
            delay(2000)
            if (isLoggedIn == true) {
                navController.navigate("sleep_setting") { popUpTo("splash") { inclusive = true } }
            } else {
                navController.navigate("login") { popUpTo("splash") { inclusive = true } }
            }
        }
    }

    // 베경 그라데이션
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
    ) {
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
        // 별패턴
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )

        // 로고 + 텍스트 중앙 배치
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sleephony_logo),
                    contentDescription = "Sleephony Logo",
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Text(
                    text = "슬립포니와 함께 오늘도 편안한 꿈나라로 떠나보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFEEEEEE),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp).offset()
                )
            }
        }
    }
}

