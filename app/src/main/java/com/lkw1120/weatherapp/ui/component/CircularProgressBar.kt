package com.lkw1120.weatherapp.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lkw1120.weatherapp.ui.theme.LightBlue700

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = LightBlue700,
        strokeWidth = strokeWidth
    )
}