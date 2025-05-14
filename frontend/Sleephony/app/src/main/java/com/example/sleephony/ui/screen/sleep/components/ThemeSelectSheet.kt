package com.example.sleephony.ui.screen.sleep.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sleephony.R
import com.example.sleephony.data.model.theme.ThemeListResult

@Composable
fun ThemeSelectSheet(
    themes: List<ThemeListResult>,
    currentThemeId: Int,
    onThemeSelected: (ThemeListResult) -> Unit,
    onPlay: (ThemeListResult) -> Unit,
    onStop: () -> Unit,
    playingThemeId: Int?,
    onClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1) 배경 딤 + 클릭 시 닫기
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0x80000000))
                .clickable { onClose() }
        )

        // 2) 시트 카드
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 700.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFF314282))
            ) {
                // 헤더: 현재 선택된 테마
                item {
                    themes.firstOrNull { it.id == currentThemeId }?.let { current ->
                        Box(modifier = Modifier.height(200.dp)) {
                            AsyncImage(
                                model = current.imageUrl,
                                contentDescription = current.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "닫기",
                                    tint = Color.White
                                )
                            }
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = current.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(Modifier.height(8.dp))
                                if(playingThemeId == current.id) {
                                    IconButton(
                                        onClick = { onStop() },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(painterResource(R.drawable.ic_pause_circle2),
                                            "멈춤",
                                            tint = Color.White.copy(alpha = 0.8f),
                                            modifier = Modifier.size(48.dp))
                                    }
                                } else {
                                    IconButton(
                                        onClick = { onPlay(current) },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon( painterResource(R.drawable.ic_play_circle2),
                                            "플레이",
                                            tint = Color.White.copy(alpha = 0.8f),
                                            modifier = Modifier.size(48.dp))
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // 리스트: 모든 테마
                items(themes) { theme ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onThemeSelected(theme) }
                    ) {
                        AsyncImage(
                            model = theme.imageUrl,
                            contentDescription = theme.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                        )
                        Text(
                            text = theme.name,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Text(
                        text = theme.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
