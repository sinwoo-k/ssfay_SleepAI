package com.example.sleephony.ui.screen.auth

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.navigation.ProfileStep
import com.example.sleephony.ui.theme.SleephonyTheme


data class StepConfig(
    val label: String,
    val subText: String,
    val placeholder: String,
    val keyboardType: KeyboardType
)

@Composable
fun ProfileSetupScreen(
    step: ProfileStep,
    onNext: () -> Unit,
    onComplete: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
){
    val values by viewModel.stepValues.collectAsState(initial = emptyMap())
    val current = values[step].orEmpty()

    // step별 힌트/레이블 설정
    val (label, subText, placeholder, keyboard) = when(step) {
        ProfileStep.NICKNAME -> StepConfig("닉네임을 입력하세요.", "어떻게 불러야할지 알려주세요:)","포니", KeyboardType.Text)
        ProfileStep.BIRTHDAY -> StepConfig("생년월일을 입력하세요.", "생일을 알려주세요:)","YYYY.MM.DD", KeyboardType.Number)
        ProfileStep.GENDER   -> StepConfig("성별을 선택해주세요.", "수면 패턴 측정에 필요합니다.","남자/여자", KeyboardType.Text)
        ProfileStep.HEIGHT   -> StepConfig("키를 입력해주세요.", "수면 패턴 측정에 필요합니다.","cm", KeyboardType.Number)
        ProfileStep.WEIGHT   -> StepConfig("몸무게를 입력해주세요.", "수면 패턴 측정에 필요합니다.","kg", KeyboardType.Number)

    }

    val submitResult by viewModel.submitState.collectAsState()

    LaunchedEffect(submitResult) {
        submitResult?.onSuccess { onComplete() }
        submitResult?.onFailure { Log.e("ProfileSetup", "프로필 저장 실패", it) }
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
    ) {
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

            Text(text = label, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(Modifier.height(5.dp))
            Text(text = subText, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)

            Spacer(Modifier.height(20.dp))

            BasicTextField(
                value = current,
                onValueChange = { viewModel.updateValue(step, it) },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (current.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.headlineSmall.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                },
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
                onClick = onNext,
                enabled = viewModel.isValid(step),
                shape =  RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5067B4),
//                    disabledContainerColor = Color(0xFF5067B4)
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 4.dp                 // 그림자 높이
                )
            ) {
                Text("확인", color = Color.White)
            }
        }
    }
}


