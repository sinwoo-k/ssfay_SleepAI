package com.example.sleephony.ui.screen.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GuideBox(
    title: String,
    image: Painter,
    imageDescription: String,
    content: String
){
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
        )
        Spacer(Modifier.height(8.dp))
        Image(
            painter = image,
            contentDescription = imageDescription,
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = content,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}