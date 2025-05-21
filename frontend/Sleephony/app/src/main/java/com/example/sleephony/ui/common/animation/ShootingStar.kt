package com.example.sleephony.ui.common.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.hypot

@Composable
fun ShootingStar(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    durationMillis: Int = 1200,
    startXFrac: Float = 0.2f,
    startYFrac: Float = 0.1f,
    endXFrac: Float = 0.8f,
    endYFrac: Float = 0.5f,
    pointOnlyFraction: Float = 0.2f,
    trailFraction: Float = 0.1f,
    thickness: Float = 8f
) {
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, delayMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 시작점, 끝점
        val start = Offset(startXFrac * w, startYFrac * h)
        val end   = Offset(endXFrac   * w, endYFrac   * h)

        // 현재 위치
        val cx = start.x + (end.x - start.x) * progress
        val cy = start.y + (end.y - start.y) * progress
        val current = Offset(cx, cy)

        // 시작→끝 벡터
        val dx = end.x - start.x
        val dy = end.y - start.y
        // 벡터 길이
        val dist = hypot(dx, dy)
        // 단위 벡터
        val ux = dx / dist
        val uy = dy / dist

        val rawAlpha = progress
        val starColor = Color.Gray.copy(alpha = rawAlpha)

        if (progress < pointOnlyFraction) {
            // 점만 그리기
            drawCircle(
                color = starColor,
                center = current,
                radius = thickness / 2f,
                style = Stroke(width = thickness)
            )
        } else {
            // 꼬리 길이 (전체 꼬리 길이 × 진행 비율)
            val effectiveProgress = (progress - pointOnlyFraction) / (1f - pointOnlyFraction)
            val maxTailLen = trailFraction * dist
            val tailLen    = maxTailLen * effectiveProgress

            // 단위 벡터 방향으로 꼬리 시작점 계산
            val tailStart = Offset(
                x = cx - ux * tailLen,
                y = cy - uy * tailLen
            )

            // 꼬리 그리기
            drawLine(
                color = starColor,
                start = tailStart,
                end = current,
                strokeWidth = thickness
            )
        }
    }
}
