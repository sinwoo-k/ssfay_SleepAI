package com.example.sleephony_wear.presentation.components.alarm

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleepphony_wear_os.R
import com.example.sleepphony_wear_os.presentation.theme.bluePuple
import com.example.sleepphony_wear_os.presentation.theme.darkNavyBlue


@Composable
fun AlarmScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(37.dp)
                        .shadow( elevation = 40.dp, shape = RoundedCornerShape(12.dp)),
                    onClick = {navController.navigate("sleepalarm")},
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isPressed) bluePuple else darkNavyBlue)
                ) {
                    Text(text = stringResource(R.string.compotable_alarm_mode))
                }

                val interactionSource2 = remember { MutableInteractionSource() }
                val isPressed2 by interactionSource2.collectIsPressedAsState()

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(37.dp)
                        .shadow( elevation = 20.dp, shape = RoundedCornerShape(12.dp)),
                    interactionSource = interactionSource2,
                    onClick = {navController.navigate("setalarm")},
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isPressed2) bluePuple else darkNavyBlue)
                ) {
                    Text(text = stringResource(R.string.alarm_mode))
                }

                val interactionSource3 = remember { MutableInteractionSource() }
                val isPressed3 by interactionSource3.collectIsPressedAsState()

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(37.dp)
                        .shadow( elevation = 30.dp, shape = RoundedCornerShape(12.dp)),
                    interactionSource = interactionSource3,
                    onClick = {navController.navigate("setalarm")},
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if ( isPressed3) bluePuple else darkNavyBlue,
                        )
                ) {
                    Text(text = stringResource(R.string.sleep_mode))
                }
            }
        }
    }
