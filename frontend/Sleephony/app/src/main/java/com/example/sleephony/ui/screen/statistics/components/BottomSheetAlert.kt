package com.example.sleephony.ui.screen.statistics.components

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.sleephony.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAlert(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    content: @Composable (() -> Unit)
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            containerColor = colorResource(R.color.dark_navy),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp,Alignment.CenterVertically)
            ) {
                content()
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape =  RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5063D4))
                ) {
                    Text(
                        text = "확인",
                        color = Color.White,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}
