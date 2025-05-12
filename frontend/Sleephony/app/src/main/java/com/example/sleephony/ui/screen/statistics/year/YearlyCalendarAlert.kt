package com.example.sleephony.ui.screen.statistics.year

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.detail.White_text
import java.time.LocalDate
import java.time.Year

@Composable
fun YearlyCalendarAlert(
    yearState: LocalDate,
    modifier: Modifier = Modifier,
    onRequest: () -> Unit,
    changRequest: (LocalDate) -> Unit
) {
    val currentYear = remember { Year.now().value }
    val startYear = currentYear - 20
    val endYear = currentYear + 20
    val years = (startYear..endYear).toList()

    val initialIndex = years.indexOf(yearState.year).coerceAtLeast(0)
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        listState.scrollToItem(index = (initialIndex - 3).coerceAtLeast(0))
    }

    AlertDialog(
        onDismissRequest = onRequest,
        confirmButton = {},
        containerColor = colorResource(R.color.dark_navy),
        title = {
            White_text("연도 선택")
        },
        text = {
            LazyColumn(
                state = listState,
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(years) { year ->
                    Text(
                        textAlign = TextAlign.Center,
                        text = "${year}년",
                        fontSize = 30.sp,
                        fontWeight = if (year == yearState.year) FontWeight.Bold else FontWeight.Normal,
                        color = if (year == yearState.year) Color.White else Color.White.copy(alpha = .3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                changRequest(LocalDate.of(year, 1, 1))
                                onRequest()
                            }
                            .padding(5.dp)
                    )
                }
            }
        }
    )
}
