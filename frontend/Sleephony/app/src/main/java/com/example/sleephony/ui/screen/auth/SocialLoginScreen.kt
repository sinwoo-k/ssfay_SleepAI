package com.example.sleephony.ui.screen.auth

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.sleephony.ui.common.components.SocialLoginButton


@Composable
fun SocialLoginScreen(
    onLoginSuccess: () -> Unit,
    onNeedsProfile: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val activity = LocalActivity.current

    // 1) 로그인 상태에 따라 네비게이션
    LaunchedEffect(state) {
        when (state) {
            is AuthViewModel.UiState.NeedsProfile -> onNeedsProfile()
            is AuthViewModel.UiState.Authenticated -> onLoginSuccess()
            is AuthViewModel.UiState.Error ->
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> { }
        }
    }

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
        // 배경 별 이미지 (원한다면 투명도나 높이 조정)
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )

        // 로고 + 텍스트를 화면 중앙에
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sleephony_logo),
                    contentDescription = "Sleephony Logo",
                    modifier = Modifier
                        .width(400.dp)
                        .height(200.dp)
                )
                Text(
                    text = "슬립포니와 함께 오늘도 편안한 꿈나라로 떠나보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFEEEEEE),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                )
            }
        }

        // 하단 소셜 로그인 버튼
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp)
                .offset(y = (-100).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SocialLoginButton(
                text = "카카오 로그인",
                iconRes = R.drawable.ic_kakao,
                backgroundColor = Color(0xFFFEE500),
                contentColor = Color(0xFF191919),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { activity?.let { viewModel.signInWithKakao(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginButton(
                text = "구글 로그인",
                iconRes = R.drawable.ic_google,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { activity?.let { viewModel.signInWithGoogle(it) } }
            )
        }
    }
}
