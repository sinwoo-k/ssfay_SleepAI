package com.example.sleephony.ui.common.components

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sleephony.R

sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    data object Sleep      : BottomNavItem("sleep_setting", R.drawable.ic_nights_stay,  "수면")
    data object Report     : BottomNavItem("report",        R.drawable.ic_description, "리포트")
    data object Statistics : BottomNavItem("statistics",    R.drawable.ic_insert_chart,    "통계")
    data object Settings   : BottomNavItem("settings",      R.drawable.ic_settings,    "설정")
}

@Composable
fun BottomNavBar(navController: NavHostController){

    // Activity 레퍼런스
    val activity = LocalActivity.current

    // 뒤로가기 핸들러
    BackHandler {
        activity?.finish()
    }

    val items = listOf(
        BottomNavItem.Sleep,
        BottomNavItem.Report,
        BottomNavItem.Statistics,
        BottomNavItem.Settings
    )
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF1D1437),
        contentColor   = Color.White,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon    = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label   = { Text(item.title) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    indicatorColor      = Color.Transparent,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.5f)
                ),
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // 스택 정리 및 상태 복원
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                }
            )
        }
    }
}