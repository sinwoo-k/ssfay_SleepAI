package com.example.sleephony.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R

@Composable
fun SettingsHomeScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    logout: () -> Unit
){
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
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )
        val profile by viewModel.profileState.collectAsState()
        Column(

        ) {
            if (profile != null ) {
                Text(text = "이메일 : ${profile!!.email}", color = Color.White)
            } else {
                Text("프로필 정보를 불러오는 중입니다.")
            }
            TextButton(
                onClick = {
                    viewModel.logout()
                    logout()
                }

            ) {
                Text("로그아웃", color = Color.Red)
            }
        }

    }
}