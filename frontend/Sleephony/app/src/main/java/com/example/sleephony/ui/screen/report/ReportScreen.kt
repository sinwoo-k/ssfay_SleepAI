package com.example.sleephony.ui.screen.report

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.component.ReportCalendar
import com.example.sleephony.ui.screen.report.component.ReportContent
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import java.time.LocalDate
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel
) {
    // ReportScreen에서 selectedDate 관리
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

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
        // 배경 별 이미지
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )

        // 달력 + 리포트 영역
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            // 📅 달력 영역: 고정 높이
            ReportCalendar(
                modifier = Modifier.fillMaxWidth(),
                navController = navController,
                viewModel = reportViewModel,
                selectedDate = selectedDate,  // 선택된 날짜 전달
                onDateSelected = { date ->
                    selectedDate = date
                    println("Selected Date: $selectedDate") // 날짜 변경 시 콘솔에 출력
                    Log.d("ReportScreen", "Selected Date: $selectedDate") // 로그에도 출력
                }  // 날짜 선택 시 업데이트
            )

            // 🛌 리포트 영역: 남은 공간 모두 사용
            ReportContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                reportViewModel = reportViewModel,
                selectedDate = selectedDate
            )
        }
    }
}
