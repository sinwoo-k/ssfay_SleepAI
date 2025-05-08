package com.example.sleephony.ui.screen.report.components.detail.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.sleephony.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun ComparisonBarChart(
    modifier: Modifier = Modifier,
    days: List<String>,
    sleepHours:List<Float>
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false // 차트 설명
                setDrawGridBackground(false) // 그리드 배경
                setDrawBarShadow(false) // 막대 그림자
                setDrawValueAboveBar(false) // 값 표시 위치 설정
                setPinchZoom(false) // 핀치 줌 기능
                setTouchEnabled(false) // 스케일(확대/축소) 기능
                setScaleEnabled(false) // 스케일(확대/축소) 기능
                setDragEnabled(false) // 드래그 기능

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM //  X축 위치
                    granularity = 1f // 축 값 사이의 최소 간격 설정
                    setDrawGridLines(false) // : X축 그리드 라인
                    valueFormatter = IndexAxisValueFormatter(days) // X축 목록 적용
                    textColor = android.graphics.Color.WHITE //  X축 텍스트 색상
                }

                axisLeft.apply {
                    setDrawGridLines(true) // Y축 그리드 라인
                    setDrawAxisLine(false) // Y축 라인
                    axisMinimum = 0f // Y축 최소값
                    textColor = android.graphics.Color.WHITE  // Y축 텍스트 색상


                    val myAverageLine = LimitLine(8.05f, "").apply {
                        lineColor = ContextCompat.getColor(context,R.color.light_gray)
                        lineWidth = 2f
                        textColor = android.graphics.Color.WHITE
                        textSize = 12f
                    }

                    val maleAverageLine = LimitLine(6.95f, "").apply {
                        lineColor = ContextCompat.getColor(context,R.color.dark_navy)
                        lineWidth = 2f
                        textColor = android.graphics.Color.WHITE
                        textSize = 12f
                    }

                    addLimitLine(myAverageLine)
                    addLimitLine(maleAverageLine)
                }



                axisRight.isEnabled = false // 오른쪽 y 축
                legend.isEnabled = false // 범례

                setDrawGridBackground(false) // 그리드 설정
                setBackgroundColor(android.graphics.Color.TRANSPARENT) // 배경 설정
                setExtraOffsets(10f, 10f, 10f, 10f) // 차트 주변에 여백 추가
            }
        },
        update = { chart ->
            val entries = sleepHours.mapIndexed { index, hours ->
                BarEntry(index.toFloat(), hours)
            }

            val dataSet = BarDataSet(entries, "수면 시간").apply {
                color = ContextCompat.getColor(chart.context, R.color.SkyBlue)
                valueTextSize = 0f
            }

            val barData = BarData(dataSet).apply {
                barWidth = 0.5f
            }

            chart.data = barData
            chart.invalidate()
        }
    )
}