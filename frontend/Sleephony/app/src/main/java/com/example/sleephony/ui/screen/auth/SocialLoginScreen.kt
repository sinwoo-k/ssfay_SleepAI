package com.example.sleephony.ui.screen.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.ui.common.component.SocialLoginButton


@Composable
fun SocialLoginScreen(
   onLoginSuccess: () -> Unit,
   viewModel: AuthViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ) {
        // 1) 상단: 배경 별패턴 + 로고 + 텍스트
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 별패턴 이미지
            Image(
                painter = painterResource(R.drawable.bg_stars),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.FillWidth
            )

            // 로고 + 텍스트 중앙 배치
            Box(
                modifier = Modifier
                    .offset(y = (-100).dp)
                    .size(400.dp),
                contentAlignment = Alignment.Center
            ) {
                // 로고
                Image(
                    painter = painterResource(R.drawable.ic_sleephony_logo),
                    contentDescription = "Sleepphony Logo",
                    modifier = Modifier
                        .size(400.dp)
                )
                Text(
                    text = "슬립포니와 함께 오늘도 편안한 꿈나라로 떠나보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFEEEEEE),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = 120.dp)
                        .padding(horizontal = 32.dp)
                )
            }
        }

        // 2) 하단: 소셜 로그인 버튼들
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y= (-100).dp)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val activity = LocalActivity.current
            // 카카오 버튼
            SocialLoginButton(
                text = "카카오 로그인",
                iconRes = R.drawable.ic_kakao,
                backgroundColor = Color(0xFFFEE500),
                contentColor = Color(0xFF191919),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    Log.d("DBG","button")
                    activity?.let {
                    viewModel.signInWithKakao(it)  }  }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 구글 버튼
            SocialLoginButton(
                text = "구글 로그인",
                iconRes = R.drawable.ic_google,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {}
            )
        }
    }
}