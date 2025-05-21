package com.example.sleephony.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

private const val REPEAT_COUNT = 1000

@Composable
fun LoopingWheelPicker(
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 5,
    itemHeight: Dp = 50.dp,
    initialIndex: Int = 0,
    selectedTextStyle: TextStyle = TextStyle(fontSize = 32.sp, color = Color.White),
    unselectedTextStyle: TextStyle = TextStyle(fontSize = 30.sp, color = Color.Gray),
    onItemSelected: (index: Int) -> Unit
) {
    val realCount    = if (visibleItemCount % 2 == 0) visibleItemCount + 1 else visibleItemCount
    val halfCount    = realCount / 2
    val totalItems   = items.size * REPEAT_COUNT
    val density      = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.roundToPx() }

    val initialIdx   = totalItems / 2 + initialIndex - halfCount
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIdx,
        initialFirstVisibleItemScrollOffset = 0
    )

    val centerIndex   by remember { derivedStateOf { listState.firstVisibleItemIndex + halfCount } }
    val selectedIndex by remember { derivedStateOf { (centerIndex % items.size + items.size) % items.size } }
    LaunchedEffect(selectedIndex) { onItemSelected(selectedIndex) }

    val snapper       = remember(listState) { SnapLayoutInfoProvider(listState, SnapPosition.Center) }
    val flingBehavior: FlingBehavior = rememberSnapFlingBehavior(snapper)

    Box(modifier = modifier.height(itemHeight * realCount)) {
        LazyColumn(
            state         = listState,
            flingBehavior = flingBehavior,
            modifier      = Modifier.fillMaxWidth()
        ) {
            items(totalItems) { idx ->
                val realIdx    = (idx % items.size + items.size) % items.size
                val isSelected = realIdx == selectedIndex
                val alpha      by animateFloatAsState(if (isSelected) 1f else 0.4f, tween(200))
                val color      by animateColorAsState(if (isSelected) selectedTextStyle.color else unselectedTextStyle.color, tween(200))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text      = items[realIdx],
                        style     = TextStyle(
                            fontSize = if (isSelected) selectedTextStyle.fontSize else unselectedTextStyle.fontSize,
                            color    = color
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
