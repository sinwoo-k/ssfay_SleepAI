package com.example.sleephony.components.mypage

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkGray
import com.example.sleephony.presentation.theme.darkNavyBlue


@Composable
fun MyPageScreen(
    navController: NavController,
    modifier: Modifier
) {
    val context = LocalContext.current
    val profile = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    val email = profile.getString("email", null) ?: ""
    val nickname = profile.getString("nickname", null) ?: ""
    val height = profile.getString("height", null) ?: ""
    val weight = profile.getString("weight", null) ?: ""
    val birthDate = profile.getString("birthDate", null) ?: ""
    val gender = profile.getString("gender", null) ?: ""


    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        reverseLayout = true
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                        .padding(12.dp,5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = email, fontWeight = FontWeight.Bold)
                }
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
                            Text(text = stringResource(id = R.string.name), fontSize = 13.sp )
                            Text(text = nickname, fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                            ) {
                            Text(text = stringResource(id = R.string.birthday), fontSize = 13.sp )
                            Text(text = birthDate, fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.gender), fontSize = 13.sp )
                            Text(text = stringResource(if (gender == "M")R.string.man else R.string.woman), fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.height), fontSize = 13.sp )
                            Text(text = height, fontSize = 13.sp)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.weight), fontSize = 13.sp )
                            Text(text = weight, fontSize = 13.sp)
                        }
                        Spacer(modifier = modifier.size(10.dp))
                    }
                }

            }
        }
    }
}
