package com.example.sleephony.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.ui.screen.settings.components.SettingTextButton
import com.example.sleephony.ui.screen.settings.components.UserProfileBox

@Composable
fun SettingsHomeScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    logout: () -> Unit,
    goUserProfile: () -> Unit
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

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            UserProfileBox(
                profile = profile,
                logout = {
                    viewModel.logout()
                    logout()
                }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.White.copy(0.8f),
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                SettingTextButton(
                    onClick = goUserProfile,
                    text = "개인 정보 변경"
                )
                Spacer(Modifier.height(24.dp))
                SettingTextButton(
                    onClick = {},
                    text = "스마트 워치 연동"
                )
                Spacer(Modifier.height(24.dp))
                SettingTextButton(
                    onClick = {},
                    text = "이용 가이드"
                )
            }
            Text(
                text = "Version 1.0.0",
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}
