package com.example.sleephony.ui.screen.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingTextButton(
    onClick: () -> Unit,
    text: String
){
    TextButton (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonColors(
            contentColor = Color.White,
            containerColor = Color.Black.copy(.3f),
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Black.copy(.3f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        )
    }
}