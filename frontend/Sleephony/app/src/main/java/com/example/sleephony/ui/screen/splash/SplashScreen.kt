package com.example.sleephony.ui.screen.splash

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sleephony.R
import com.example.sleephony.data.datasource.local.UserLocalDataSource
import com.example.sleephony.ui.common.animation.ShootingStar
import com.example.sleephony.ui.screen.auth.ProfileViewModel
import com.example.sleephony.ui.screen.statistics.components.detail.SummarTime
import com.example.sleephony.ui.screen.statistics.viewmodel.StatisticsViewModel
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(viewModel.isNotificationEnabled()) }

    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = null)

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // 설정 화면에서 돌아왔을 때 호출
        notificationsEnabled = viewModel.isNotificationEnabled()
        if (!notificationsEnabled && hasRequestedPermission) {
            // 두 번째(취소)라면 앱 종료
            activity?.finishAffinity()
        }
    }

    val weekStartState = statisticsViewModel.selectedWeek
    val weekEnd = weekStartState.plusDays(6)

    LaunchedEffect(Unit) {
        statisticsViewModel.loadStatistics(
            startDate = weekStartState.toString(),
            endDate = weekEnd.toString(),
            periodType = "WEEK"
        )
    }

    val statistics = statisticsViewModel.statistics.collectAsState().value
    statistics?.sleepTime
    val profile = remember {
        UserLocalDataSource(context).getProfile()
    }
    LaunchedEffect(profile,statistics) {
        profile?.let {
            SendProfile(
                context = context,
                email = it.email,
                height = it.height.toString(),
                gender = it.gender,
                weight = it.weight.toString(),
                nickname = it.nickname,
                birthDate = it.birthDate
            )
        }
        statistics?.sleepTime?.forEach {
            SendHistory(
                context = context,
                label = it.label,
                value = SummarTime(it.value.toInt())
            )
        }
    }


    LaunchedEffect(isLoggedIn, notificationsEnabled, hasRequestedPermission) {
        if (isLoggedIn != null) {
            // 알림이 꺼져 있으면 설정으로 보내고 중단
            if (!notificationsEnabled && !hasRequestedPermission) {
                hasRequestedPermission = true
                settingsLauncher.launch(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra("app_uid", context.applicationInfo.uid)
                        // FLAG_NEW_TASK는 붙이지 않습니다!
                    }
                )
                return@LaunchedEffect
            }
            // 알림 허용된 상태에서 기존 로직
            delay(2000)
            if (isLoggedIn == true) {
                navController.navigate("sleep_setting") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    // 베경 그라데이션
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ) {
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 200,
            durationMillis = 1000,
            startXFrac = 0.1f, startYFrac = 0.2f,
            endXFrac   = 0.5f, endYFrac   = 0.6f
        )
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 1500,
            durationMillis = 1000,
            startXFrac = 0.2f, startYFrac = 0.15f,
            endXFrac   = 0.9f, endYFrac   = 0.7f
        )
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 1000,
            durationMillis = 1000,
            startXFrac = 0.4f, startYFrac = 0.05f,
            endXFrac   = 0.9f, endYFrac   = 0.4f
        )
        // 별패턴
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )

        // 로고 + 텍스트 중앙 배치
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sleephony_logo),
                    contentDescription = "Sleephony Logo",
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Text(
                    text = "슬립포니와 함께 오늘도 편안한 꿈나라로 떠나보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFEEEEEE),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp).offset()
                )
            }
        }
    }
}

fun SendProfile(context: Context, email:String, nickname:String, height:String, weight:String, birthDate: String,gender:String ){
    CoroutineScope(Dispatchers.IO).launch {
        try{
            val nodeClient = Wearable.getNodeClient(context)
            val messageClient = Wearable.getMessageClient(context)

            val jsonData = JSONObject().apply {
                put("mode","profile")
                put("email",email)
                put("nickname",nickname)
                put("height",height)
                put("weight",weight)
                put("birthDate",birthDate)
                put("gender",gender)
            }
            val jsonString = jsonData.toString()

            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/alarm",
                    "$jsonString ".toByteArray()
                ).await()
            }
        } catch (error: Exception){
            Log.e("ssafy","$error")
        }
    }
}

fun SendHistory(context: Context, label:String, value: String){
    CoroutineScope(Dispatchers.IO).launch {
        try{
            val nodeClient = Wearable.getNodeClient(context)
            val messageClient = Wearable.getMessageClient(context)

            val jsonData = JSONObject().apply {
                put("mode","history")
                put("day",label)
                put("value",value)
            }
            val jsonString = jsonData.toString()

            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/alarm",
                    "$jsonString ".toByteArray()
                ).await()
            }
            Log.d("ssafy","send history success")
        } catch (error: Exception){
            Log.e("ssafy","$error")
        }
    }
}