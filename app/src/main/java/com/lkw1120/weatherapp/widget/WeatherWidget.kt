package com.lkw1120.weatherapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.MainActivity
import com.lkw1120.weatherapp.ui.component.getIconResByIconId
import com.lkw1120.weatherapp.ui.theme.BlueGray900

class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {

            val prefs = currentState<Preferences>()

            WidgetContent(
                locationName = prefs[WeatherWidgetReceiver.locationName] ?: "Unknown",
                weatherIcon = prefs[WeatherWidgetReceiver.weatherIcon] ?: "",
                weatherTemp = prefs[WeatherWidgetReceiver.weatherTemp] ?: "--",
                todayMax = prefs[WeatherWidgetReceiver.todayMax] ?: "--",
                todayMin = prefs[WeatherWidgetReceiver.todayMin] ?: "--",
                description = prefs[WeatherWidgetReceiver.description] ?: ""
            )
        }
    }
}

@Composable
fun WidgetContent(
    locationName: String,
    weatherIcon: String,
    weatherTemp: String,
    todayMax: String,
    todayMin: String,
    description: String
) {

    Box(
        modifier = GlanceModifier
            .wrapContentSize()
            .background(ColorProvider(Color(0xFF, 0xFF, 0xFF, 0x77)))
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .clickable(actionStartActivity(activity = MainActivity::class.java))
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {

            Image(
                provider = ImageProvider(getIconResByIconId(iconId = weatherIcon)),
                contentDescription = null,
                modifier = GlanceModifier
                    .wrapContentSize()
                    .padding(vertical = 18.dp)
            )
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
            ) {
                Text(
                    modifier = GlanceModifier
                        .wrapContentSize(),
                    text = locationName,
                    style = TextStyle(
                        fontSize = 21.sp,
                        color = ColorProvider(BlueGray900)
                    ),
                )

                Text(
                    modifier = GlanceModifier
                        .wrapContentSize(),
                    text = "$weatherTemp${AppStrings.degree}",
                    style = TextStyle(
                        fontSize = 56.sp,
                        color = ColorProvider(BlueGray900)
                    )
                )

                Text(
                    modifier = GlanceModifier
                        .wrapContentSize(),
                    text = "$todayMin${AppStrings.degree}/$todayMax${AppStrings.degree}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = ColorProvider(BlueGray900)
                    )
                )

                Text(
                    modifier = GlanceModifier
                        .wrapContentSize(),
                    text = description,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = ColorProvider(BlueGray900)
                    )
                )
            }
        }
    }
}