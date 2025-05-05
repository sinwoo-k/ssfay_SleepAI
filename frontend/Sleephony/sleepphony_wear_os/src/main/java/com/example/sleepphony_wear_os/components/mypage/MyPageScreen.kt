package com.example.sleephony_wear.components.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleepphony_wear_os.R
import com.example.sleepphony_wear_os.presentation.theme.darkGray
import com.example.sleepphony_wear_os.presentation.theme.darkNavyBlue


@Composable
fun MyPageScreen(
    navController: NavController,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    contentDescription = "프로필",
                    painter = painterResource(id = R.drawable.user),
                    modifier = modifier.size(40.dp)
                )
                Text(text = stringResource(id = R.string.username), fontWeight = FontWeight.Bold)
                Box(
                    modifier = modifier.fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(darkGray),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                            ) {
                            val birthDay = stringResource(id = R.string.birthday_year) +"."+
                                    stringResource(id = R.string.birthday_month) +"."+
                                    stringResource(id = R.string.birthday_day)

                            Text(text = stringResource(id = R.string.birthday), fontSize = 13.sp )
                            Text(text = birthDay, fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.gender), fontSize = 13.sp )
                            Text(text = stringResource(id = R.string.gender_value), fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.height), fontSize = 13.sp )
                            Text(text = stringResource(id = R.string.height_value), fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.weight), fontSize = 13.sp )
                            Text(text = stringResource(id = R.string.weight_value), fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.connect_device), fontSize = 13.sp )
                            Text(text = stringResource(id = R.string.connect_device_value), fontSize = 13.sp)
                        }
                        Button(
                            onClick = {},
                            modifier = modifier.fillMaxWidth(.5f)
                                .height(30.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = ButtonDefaults.buttonColors(darkNavyBlue)
                        ) {
                            Text(text = stringResource(R.string.device_change))
                        }
                        Spacer(modifier = modifier.size(10.dp))
                    }
                }

            }
        }
    }
}
