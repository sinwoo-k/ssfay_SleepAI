package com.example.sleephony.ui.screen.report.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R

@Composable
fun SleepSummation(
    modifier: Modifier
) {
    val alphaWhite = Color.White.copy(alpha = .7f)

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.sleep_summation), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Box(modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box() {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = modifier.size(18.dp)
                                .background(shape = RoundedCornerShape(100.dp), color = colorResource(R.color.sand)))
                            Text(text = stringResource(R.string.non_sleep),fontSize = 20.sp, color = alphaWhite)
                        }
                    }
                    Text(text = "13분(3%)",fontSize = 20.sp, color = alphaWhite)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box() {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = modifier.size(18.dp)
                                .background(shape = RoundedCornerShape(100.dp), color = colorResource(R.color.purple)))
                            Text(text = stringResource(R.string.REM_sleep),fontSize = 20.sp, color = alphaWhite)
                        }
                    }
                    Text(text = "1시간 37분(22%)",fontSize = 20.sp, color = alphaWhite)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box() {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = modifier.size(18.dp)
                                .background(shape = RoundedCornerShape(100.dp), color = colorResource(R.color.indigo)))
                            Text(text = stringResource(R.string.light_sleep),fontSize = 20.sp, color = alphaWhite)
                        }
                    }
                    Text(text = "4시간 24분(62%)",fontSize = 20.sp, color = alphaWhite)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box() {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = modifier.size(18.dp)
                                .background(shape = RoundedCornerShape(100.dp), color = colorResource(R.color.deep_sea_blue)))
                            Text(text = stringResource(R.string.deep_sleep),fontSize = 20.sp, color = alphaWhite)
                        }
                    }
                    Text(text = "2시간 23분(13%)",fontSize = 20.sp, color = alphaWhite)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box() {
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = modifier.size(18.dp)
                                .background(shape = RoundedCornerShape(100.dp), color = colorResource(R.color.light_gray)))
                            Text(text = stringResource(R.string.sleep_cycle),fontSize = 20.sp, color = alphaWhite)
                        }
                    }
                    Text(text = "3회",fontSize = 20.sp, color = alphaWhite)
                }
            }
        }
    }
}