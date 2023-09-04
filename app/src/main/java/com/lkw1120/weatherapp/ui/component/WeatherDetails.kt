package com.lkw1120.weatherapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDetails(
    title1: String,
    value1: String,
    background1: Painter? = null,
    title2: String,
    value2: String,
    background2: Painter? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DetailSmallItem(title = title1, value = value1, background = background1)
        DetailSmallItem(title = title2, value = value2, background = background2)
    }
}

@Composable
private fun DetailSmallItem(
    title: String,
    value: String,
    background: Painter?,
) {
    val configuration = LocalConfiguration.current
    val cardSize = (configuration.screenWidthDp.dp / 2) - 18.dp
    Card(
        modifier = Modifier
            .size(cardSize),
        shape = RoundedCornerShape(12.dp),
        border = null,
        colors = CardDefaults
            .cardColors(containerColor = Color.White.copy(0.4f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (background != null) {
                Image(
                    painter = background,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        modifier = Modifier,
                        text = title,
                        fontSize = 16.sp
                    )
                }
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        modifier = Modifier,
                        text = value,
                        fontSize = 32.sp
                    )
                }
            }
        }
    }
}