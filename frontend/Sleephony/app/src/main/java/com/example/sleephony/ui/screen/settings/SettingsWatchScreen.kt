package com.example.sleephony.ui.screen.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.ui.screen.settings.components.SettingsTitle
import kotlinx.coroutines.delay

@Composable
fun SettingsWatchScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    backSettingHome: () -> Unit
) {
    val nodes by viewModel.wearableNodes.collectAsState(initial = emptyList())

    // 회전 애니메이션
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    val animatedAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 600)
    )

    var isRefreshing by remember { mutableStateOf(false) }

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
                .fillMaxSize()
                .padding(vertical = 48.dp)
        ) {
            SettingsTitle(
                title = "연결된 스마트 워치",
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
                        .height(500.dp)
                        .background(color = Color.Black.copy(.3f), shape = RoundedCornerShape(20.dp)),
                ) {
                    if (nodes.isEmpty()) {
                        Text(
                            text = "연결된 워치가 없습니다.",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(nodes){ node ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_smartwatch),
                                        contentDescription = "스마트워치 아이콘",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Text(
                                        text = node.displayName,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                }

                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        if(isRefreshing) return@TextButton
                        isRefreshing = true
                        rotationAngle += 360f
                        viewModel.refreshConnectedWearOsNodes()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = "새로고침 아이콘",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp).rotate(animatedAngle)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "새로고침 ",
                        color = Color.Gray
                    )
                }
            }
        }
        LaunchedEffect(isRefreshing) {
            if(isRefreshing){
                delay(800)
                isRefreshing = false
            }
        }
    }
}
