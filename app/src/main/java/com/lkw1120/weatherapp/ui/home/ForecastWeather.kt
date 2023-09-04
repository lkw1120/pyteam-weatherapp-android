package com.lkw1120.weatherapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.component.getIconResByIconId
import com.lkw1120.weatherapp.usecase.model.weather.Daily
import com.lkw1120.weatherapp.usecase.model.weather.Hourly
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun WeatherHourlySection(
    modifier: Modifier = Modifier,
    hourlyList: List<Hourly>,
    timezone: String
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        border = null,
        colors = CardDefaults
            .cardColors(containerColor = Color.White.copy(0.4f))
    ) {
        Column {
            Text(
                text = AppStrings.hourly_forecast,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 12.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .horizontalScroll(scrollState)
            ) {
                hourlyList.forEach {
                    WeatherHourlyItem(
                        hourly = it,
                        timezone = timezone,
                        modifier = Modifier
                            .width(90.dp)
                            .height(130.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDailySection(
    modifier: Modifier = Modifier,
    dailyList: List<Daily>,
    timezone: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        border = null,
        colors = CardDefaults
            .cardColors(containerColor = Color.White.copy(0.4f))
    ) {
        Column {
            Text(
                text = AppStrings.daily_forecast,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 12.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
            Column(
                modifier = Modifier
                    .height(512.dp)
            ) {
                dailyList.forEach {
                    WeatherDailyItem(
                        daily = it,
                        timezone = timezone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherHourlyItem(
    modifier: Modifier = Modifier,
    hourly: Hourly,
    timezone: String
) {
    val weather = hourly.weather?.get(0)
    val iconId = weather?.icon!!
    val dt = LocalDateTime
        .ofInstant(
            hourly.dt?.toLong()?.times(1000)?.let { Instant.ofEpochMilli(it) },
            ZoneId.of(timezone)
        )
        .format(DateTimeFormatter.ofPattern("hh a", Locale.ENGLISH))


    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dt,
                modifier = Modifier
                    .wrapContentSize(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Image(
                painter = painterResource(id = getIconResByIconId(iconId = iconId)),
                contentDescription = null,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = "${hourly.temp?.toInt()}${AppStrings.degree}",
                modifier = Modifier
                    .wrapContentSize(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun WeatherDailyItem(
    modifier: Modifier = Modifier,
    daily: Daily,
    timezone: String
) {
    val weather = daily.weather?.get(0)
    val temp = daily.temp
    val iconId = weather?.icon!!
    val dt = LocalDateTime
        .ofInstant(
            daily.dt?.toLong()?.times(1000)?.let { Instant.ofEpochMilli(it) },
            ZoneId.of(timezone)
        )
        .format(DateTimeFormatter.ofPattern("E", Locale.ENGLISH))


    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dt,
                modifier = Modifier
                    .fillMaxWidth(0.25f),
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Image(
                painter = painterResource(id = getIconResByIconId(iconId = iconId)),
                contentDescription = null,
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "${temp?.min?.toInt()}${AppStrings.degree} / ${temp?.max?.toInt()}${AppStrings.degree}",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}