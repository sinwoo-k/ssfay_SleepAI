package com.example.sleephony.ui.common.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@Composable
fun LoopingWheelPicker(
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 5,
    itemHeight: Dp = 50.dp,
    initialIndex: Int = 0,
    selectedTextStyle: TextStyle = TextStyle(fontSize = 32.sp, color = Color.White),
    unselectedTextStyle: TextStyle = TextStyle(fontSize = 30.sp, color = Color.White),
    onItemSelected: (index: Int) -> Unit
) {
    val listState = rememberLazyListState()
    val realCount  = if (visibleItemCount % 2 == 0) visibleItemCount + 1 else visibleItemCount
    val halfCount  = realCount / 2

    // initialIndex가 바뀔 때마다 중앙에 스크롤
    LaunchedEffect(initialIndex) {
        val loopCenter  = Int.MAX_VALUE / 2
        val offsetBase  = loopCenter - (loopCenter % items.size)
        val targetIndex = offsetBase + initialIndex - halfCount

        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .filter { it.isNotEmpty() }
            .first()

        listState.scrollToItem(targetIndex)
    }

    val flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Center)
    val centerIdx by remember {
        derivedStateOf { listState.firstVisibleItemIndex + halfCount }
    }

    // 중심 위치가 바뀔 때마다 선택 변경 콜백
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
            state         = listState,
            flingBehavior = flingBehavior,
            modifier      = Modifier.fillMaxWidth()
        ) {
            items(Int.MAX_VALUE) { idx ->
                val realIdx = (idx % items.size + items.size) % items.size
                val isSel   = idx == centerIdx
                Box(
                    modifier = Modifier
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
