package com.example.sleephony.ui.screen.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AiReportScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // ✅ 뒤로 가기 텍스트 버튼
        Text(
            text = "← 뒤로가기",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ 본문 텍스트
        Text(
            text = "AI 분석 리포트 화면입니다.",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}
