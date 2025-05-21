package com.example.sleephony.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R

@Composable
fun SettingUserProfileRow(
    label: String,
    content: String,
    onClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = colorResource(R.color.SkyBlue),
                fontSize = 18.sp,
                modifier = Modifier.width(100.dp)
            )
            Text(
                text = content,
                color = Color.White,
                fontSize = 18.sp
            )
        }
        TextButton(
            onClick = onClick,
            colors = ButtonColors(
                disabledContentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                contentColor = colorResource(R.color.indigo),
                containerColor = Color.Transparent
            )
        ) {
            Text("변경하기", fontSize = 18.sp)
        }
    }
}