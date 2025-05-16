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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.ui.screen.settings.components.SettingUserProfileRow
import com.example.sleephony.ui.screen.settings.components.SettingsTitle

@Composable
fun SettingsUserProfileScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    backSettingHome: () -> Unit,
    deleteUser: () -> Unit,
    goUpdateScreen: (key: String) -> Unit
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp)
        ) {
            SettingsTitle(
                title = "개인 정보 변경",
                onClick = backSettingHome
            )

            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        val profile by viewModel.profileState.collectAsState()
                        SettingUserProfileRow("닉네임", profile.nickname, onClick = { goUpdateScreen("nickname") })
                        SettingUserProfileRow("생년월일" ,profile.birthDate.replace("-", "."), onClick = { goUpdateScreen("birthDate") })
                        SettingUserProfileRow("성별", if (profile.gender == "M") "남자" else "여자", onClick = { goUpdateScreen("gender") })
                        SettingUserProfileRow("키", "${profile.height} cm", onClick = { goUpdateScreen("height") })
                        SettingUserProfileRow("몸무게", "${profile.weight} kg", onClick = { goUpdateScreen("weight") })

                    }
                }
            }
            Spacer(Modifier.height(48.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                TextButton(
                    onClick = {
                        deleteUser()
                        viewModel.deleteUserInfo()
                    }
                ) {
                    Text(
                        text = "회원 탈퇴",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
