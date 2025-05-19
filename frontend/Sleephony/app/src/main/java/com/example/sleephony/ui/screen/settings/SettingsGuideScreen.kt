package com.example.sleephony.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.settings.components.GuideBox
import com.example.sleephony.ui.screen.settings.components.SettingsTitle

@Composable
fun SettingsGuideScreen(
    backSettingHome: () -> Unit
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp)
        ) {
            SettingsTitle(
                title = "이용자 가이드",
                onClick = backSettingHome
            )
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .background(color = Color.Black.copy(.3f), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    GuideBox(
                        title = "1. 알람 시간 설정",
                        image = painterResource(R.drawable.image_guide1),
                        imageDescription = "알람 시간 선택",
                        content = "기상 시간을 선택해주세요."
                    )
                    Spacer(Modifier.height(24.dp))
                    GuideBox(
                        title = "2. 테마 선택하기",
                        image =  painterResource(R.drawable.image_guide2),
                        imageDescription = "테마 선택",
                        content = "측정시 들을 테마를 선택해주세요.\n (다운로드가 필요합니다.)"
                    )
                    Spacer(Modifier.height(24.dp))
                    GuideBox(
                        title = "3. 알람 모드 선택하기 \n\n3-1. 편한 기상",
                        image = painterResource(R.drawable.image_guide3),
                        imageDescription = "편한 기상",
                        content = "설정한 시간 범위에서 알람이 울립니다.\n얕은 수면 단계에서 알람이 울립니다."
                    )
                    Spacer(Modifier.height(8.dp))
                    GuideBox(
                        title = "3-2. 정시 기상",
                        image = painterResource(R.drawable.image_guide4),
                        imageDescription = "정시 기상",
                        content = "설정한 시간에서 알람이 울립니다."
                    )
                    Spacer(Modifier.height(8.dp))
                    GuideBox(
                        title = "3-3. 알람 없음",
                        image = painterResource(R.drawable.image_guide5),
                        imageDescription = "알람 없음",
                        content = "알람이 울리지 않습니다.\n직접 측정 종료를 해야합니다."
                    )
                }
            }
        }
    }
}