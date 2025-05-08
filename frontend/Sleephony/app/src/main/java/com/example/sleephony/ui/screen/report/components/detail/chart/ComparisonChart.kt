package com.example.sleephony.ui.screen.report.components.detail.chart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R

@Composable
fun ComparisonChart(
    modifier: Modifier,
    before_name:String,
    before_value:Float,
    after_name : String,
    after_value : Float
) {
    fun change(value:Float):Float {
        val length = value.toInt().toString().length
        if (length == 2) {
            val a:Float = ((value / 100) * 150)
            Log.d("ssafy","$a")
            return a
        } else if (length == 3) {
            val a = ((value / 1000) * 150)
            Log.d("ssafy","$a")
           return a
        } else {
            return .0f
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 55.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(change(before_value).dp)
                        .background(color = colorResource(R.color.RoyalBlue), shape = RoundedCornerShape(10.dp))
                )
                Text(text = "$before_name", color = Color.White.copy(alpha = .3f))
                Text(text = "$before_value", color = Color.White.copy(alpha = .3f))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(change(after_value).dp)
                        .background(color = colorResource(R.color.SkyBlue), shape = RoundedCornerShape(10.dp))
                )
                Text(text = "$after_name", color = Color.White)
                Text(text = "$after_value", color = Color.White)
            }
        }
    }
}
