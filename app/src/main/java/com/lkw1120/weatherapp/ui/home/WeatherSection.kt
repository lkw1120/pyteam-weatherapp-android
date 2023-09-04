package com.lkw1120.weatherapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lkw1120.weatherapp.R
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.component.WeatherDetails
import com.lkw1120.weatherapp.ui.component.getIconResByIconId
import com.lkw1120.weatherapp.ui.theme.WeatherAppTheme
import com.lkw1120.weatherapp.usecase.model.weather.WeatherInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun CurrentSection(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo,
    units: String
) {

    val context = LocalContext.current

    val current = weatherInfo.weatherData?.current
    val today = weatherInfo.weatherData?.daily?.get(0)

    var isNightMode = false
    weatherInfo.weatherData?.current?.let {
        isNightMode = !(it.sunrise!! <= it.dt!! && it.dt < it.sunset!!)
    }

    val iconId = weatherInfo.weatherData?.current?.weather?.get(0)?.icon!!

    WeatherAppTheme(
        darkTheme = isNightMode
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*
            Text(
                modifier = Modifier,
                text = locationField.value,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.secondary,
            )
                Text(
                    modifier = Modifier,
                    text = name,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
             */
                Image(
                    painter = painterResource(id = getIconResByIconId(iconId = iconId)),
                    contentDescription = null,
                    modifier = Modifier
                        .width(240.dp)
                        .height(240.dp)
                        .padding(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier,
                    text = "${current?.temp?.toInt()}${AppStrings.degree}",
                    fontSize = 64.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier,
                    text = "${current?.weather?.get(0)?.description}",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier,
                    text = "${today?.temp?.min?.toInt()}${AppStrings.degree} / ${today?.temp?.max?.toInt()}${AppStrings.degree}",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun DetailSection(
    modifier: Modifier = Modifier,
    weatherInfo: WeatherInfo,
    units: String
) {
    val context = LocalContext.current

    val timezone = weatherInfo.weatherData?.timezone
    val timezoneOffset = weatherInfo.weatherData?.timezoneOffset
    val hourlyList = weatherInfo.weatherData?.hourly?.filter { item ->
        LocalDateTime
            .ofInstant(
                item.dt?.toLong()?.times(1000)?.let { Instant.ofEpochMilli(it) },
                ZoneId.of(timezone)
            )
            .hour % 3 == 0
    }
    val dailyList = weatherInfo.weatherData?.daily

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),


        ) {
        WeatherHourlySection(
            hourlyList = hourlyList ?: listOf(),
            timezone = timezone!!
        )
        Spacer(modifier = Modifier.height(12.dp))
        WeatherDailySection(
            dailyList = dailyList ?: listOf(),
            timezone = timezone
        )
        Spacer(modifier = Modifier.height(12.dp))
        WeatherDetailsSection(
            weatherInfo = weatherInfo,
            units = units
        )
        Spacer(modifier = Modifier.height(24.dp))
    }

}

@Composable
private fun WeatherDetailsSection(
    weatherInfo: WeatherInfo,
    units: String
) {

    val timezone = weatherInfo.weatherData?.timezone
    val timezoneOffset = weatherInfo.weatherData?.timezoneOffset
    val current = weatherInfo.weatherData?.current
    val temp = "${current?.temp?.toInt()}${AppStrings.degree}"
    val feelsLike = "${current?.feelsLike?.toInt()}${AppStrings.degree}"
    val uvi = "${current?.uvi}"
    val humidity = "${current?.humidity}${AppStrings.percent}"
    val clouds = "${current?.clouds}"
    val visibility =
        "${current?.visibility?.div(1000)}${if (units == "metric") AppStrings.metric else AppStrings.imperial}"
    val windSpeed =
        "${current?.windSpeed}${if (units == "metric") AppStrings.meter_per_sec else AppStrings.mile_per_hour}"
    val windDeg = when (current?.windDeg) {
        in 15 until 75 -> "NE"
        in 75 until 105 -> "E"
        in 105 until 165 -> "SE"
        in 165 until 195 -> "S"
        in 195 until 255 -> "SW"
        in 255 until 285 -> "W"
        in 285 until 345 -> "NW"
        else -> "N"
    }
    val pressure = "${current?.pressure}${AppStrings.hectopascal}"
    val sunriseTime = LocalDateTime
        .ofInstant(
            current?.sunrise?.toLong()?.times(1000)?.let { Instant.ofEpochMilli(it) },
            ZoneId.of(timezone)
        )
        .format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
    val sunsetTime = LocalDateTime
        .ofInstant(
            current?.sunset?.toLong()?.times(1000)?.let { Instant.ofEpochMilli(it) },
            ZoneId.of(timezone)
        )
        .format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
    WeatherDetails(
        title1 = AppStrings.temp,
        value1 = temp,
        background1 = painterResource(R.drawable.ic_temp),
        title2 = AppStrings.feels_like,
        value2 = feelsLike,
        background2 = painterResource(R.drawable.ic_temp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    WeatherDetails(
        title1 = AppStrings.uvi,
        value1 = uvi,
        background1 = painterResource(R.drawable.ic_solar),
        title2 = AppStrings.humidity,
        value2 = humidity,
        background2 = painterResource(R.drawable.ic_humidity)
    )
    Spacer(modifier = Modifier.height(12.dp))
    WeatherDetails(
        title1 = AppStrings.cloudiness,
        value1 = clouds,
        background1 = painterResource(R.drawable.ic_cloudy),
        title2 = AppStrings.visibility,
        value2 = visibility,
        background2 = painterResource(R.drawable.ic_binocular)
    )
    Spacer(modifier = Modifier.height(12.dp))
    WeatherDetails(
        title1 = AppStrings.sunrise,
        value1 = sunriseTime,
        background1 = painterResource(R.drawable.ic_sunrise),
        title2 = AppStrings.sunset,
        value2 = sunsetTime,
        background2 = painterResource(R.drawable.ic_sunset),
    )
    Spacer(modifier = Modifier.height(12.dp))
    WeatherDetails(
        title1 = AppStrings.wind_speed,
        value1 = windSpeed,
        background1 = painterResource(R.drawable.ic_wind_speed),
        /*
        title2 = AppStrings.pressure,
        value2 = pressure,
        background2 = painterResource(R.drawable.ic_hurricane)
         */
        title2 = AppStrings.wind_deg,
        value2 = windDeg,
        background2 = painterResource(R.drawable.ic_wind_degree)
    )
    Spacer(modifier = Modifier.height(12.dp))
}