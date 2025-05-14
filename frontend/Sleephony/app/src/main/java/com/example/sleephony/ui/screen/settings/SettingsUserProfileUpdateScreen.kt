package com.example.sleephony.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R

@Composable
fun SettingsUserProfileUpdateScreen(
    viewModel: SettingViewModel,
    key: String,
    updateProfile: () -> Unit
){

    var inputValue by remember { mutableStateOf("") }

    val title = when(key) {
        "nickname" -> "닉네임 변경하기"
        "birthDate" -> "생년월일 변경하기"
        "gender" -> "성별 변경하기"
        "height" -> "키 변경하기"
        "weight" -> "몸무게 변경하기"
        else -> ""
    }

    val keyboard = when(key) {
        "nickname" -> KeyboardType.Text
        "birthDate" -> KeyboardType.Number
        "gender" -> KeyboardType.Text
        "height" -> KeyboardType.Number
        "weight" -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ){
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height((50.dp)))

            Text(text = title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(Modifier.height(5.dp))

            Spacer(Modifier.height(20.dp))

            BasicTextField(
                value = inputValue,
                onValueChange = { newValue ->
                    inputValue = newValue
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFF6DACF0)
            )

            Spacer(Modifier.height(48.dp))

            ElevatedButton(
                onClick = {
                    viewModel.patchUserProfile(key, inputValue)
                    updateProfile()
                },
                enabled = viewModel.isValid(key, inputValue),
                shape =  RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5067B4),
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 4.dp                 // 그림자 높이
                )
            ) {
                Text("변경하기", color = Color.White)
            }
        }

    }
}

