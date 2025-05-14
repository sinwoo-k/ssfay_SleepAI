package com.example.sleephony.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sleephony.data.model.theme.ThemeListResult


@Composable
fun ThemeImageButton(
    theme: ThemeListResult,
    modifier: Modifier = Modifier,
    onClickSettings: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(40.dp))
            .clickable { /* 전체 클릭 시 동작 (예: 다이얼로그 열기) */ }
    ) {
        // 1) 배경 이미지
        AsyncImage(
            model = theme.imageUrl,
            contentDescription = theme.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
        )

        // 2) 이미지 위에 반투명 그라데이션 오버레이
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.3f)
                    )
                )
        )

        // 3) 중앙 텍스트
        Text(
            text = theme.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
        )

        // 4) 오른쪽 설정 아이콘
        IconButton(
            onClick = onClickSettings,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "테마 설정",
                tint = Color.White
            )
        }
    }
}
