package com.example.sleephony.ui.common.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoopingWheelPicker(
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 5,
    itemHeight: Dp = 40.dp,
    initialIndex: Int = 0,
    selectedTextStyle: TextStyle = TextStyle(fontSize = 32.sp, color = Color.White),
    unselectedTextStyle: TextStyle = TextStyle(fontSize = 30.sp, color = Color.White),
    onItemSelected: (index: Int) -> Unit
) {
    val realCount = if (visibleItemCount % 2 == 0) visibleItemCount + 1 else visibleItemCount
    val halfCount = realCount / 2

    val loopCount = Int.MAX_VALUE
    val loopCenter = loopCount / 2

    // ★ 초기 스크롤 위치에 -halfCount 보정 추가
    val startIndex = remember {
        (loopCenter
                - (loopCenter % items.size)
                + initialIndex
                - halfCount)                     // ← 여기를 빼 줍니다
            .coerceAtLeast(0)
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Center)

    val centerIdx by remember { derivedStateOf { listState.firstVisibleItemIndex + halfCount } }

    LaunchedEffect(centerIdx) {
        val realIdx = (centerIdx % items.size + items.size) % items.size
        onItemSelected(realIdx)
    }

    Box(
        modifier = modifier
            .height(itemHeight * realCount)
            .fillMaxWidth()
    ) {
        LazyColumn(
            state          = listState,
            flingBehavior  = flingBehavior,
            modifier       = Modifier.fillMaxWidth()
        ) {
            items(loopCount) { idx ->
                val realIdx = (idx % items.size + items.size) % items.size
                val isSel   = idx == centerIdx
                Box(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .alpha(if (isSel) 1f else 0.4f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text      = items[realIdx],
                        style     = if (isSel) selectedTextStyle else unselectedTextStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
