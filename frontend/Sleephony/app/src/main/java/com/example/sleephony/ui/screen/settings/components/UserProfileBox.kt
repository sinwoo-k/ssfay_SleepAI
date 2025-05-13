package com.example.sleephony.ui.screen.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.data.model.user.UserProfileResult

@Composable
fun UserProfileBox(
    profile: UserProfileResult
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // 프로필 박스
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_profile_character),
                        contentDescription = "프로필 캐릭터",
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = profile.nickname,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "이메일",
                            color = colorResource(R.color.SkyBlue),
                            fontSize = 18.sp,
                            modifier = Modifier.width(100.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = profile.email,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "생년월일",
                            color = colorResource(R.color.SkyBlue),
                            fontSize = 18.sp,
                            modifier = Modifier.width(100.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = profile.birthDate,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "성별",
                            color = colorResource(R.color.SkyBlue),
                            fontSize = 18.sp,
                            modifier = Modifier.width(100.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (profile.gender == "M") "남자" else "여자",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "키",
                            color = colorResource(R.color.SkyBlue),
                            fontSize = 18.sp,
                            modifier = Modifier.width(100.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "${profile.height} cm",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "몸무게",
                            color = colorResource(R.color.SkyBlue),
                            fontSize = 18.sp,
                            modifier = Modifier.width(100.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "${profile.weight} kg",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}