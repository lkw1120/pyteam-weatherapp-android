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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.MainActivity
import com.lkw1120.weatherapp.ui.component.getIconResByIconId

class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            val prefs = currentState<Preferences>()
            val icon = prefs[WeatherWidgetReceiver.icon]
            val temp = prefs[WeatherWidgetReceiver.temp]
            val name = prefs[WeatherWidgetReceiver.name]
            val max = prefs[WeatherWidgetReceiver.max]
            val min = prefs[WeatherWidgetReceiver.min]

            WidgetContent(
                widgetScreenState = WidgetScreenState(
                    icon = icon ?: "",
                    temp = temp ?: "--",
                    name = name ?: "Unknown",
                    max = max ?: "--",
                    min = min ?: "--"
                )
            )
        }
    }
}

@Composable
fun WidgetContent(
    widgetScreenState: WidgetScreenState
) {

    val name = widgetScreenState.name
    val iconId = widgetScreenState.icon
    val temp = widgetScreenState.temp
    val max = widgetScreenState.max
    val min = widgetScreenState.min

    Box(
        modifier = GlanceModifier
            .wrapContentSize()
            .background(ColorProvider(Color(0xFF, 0xFF, 0xFF, 0x99)))
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .clickable(actionStartActivity(activity = MainActivity::class.java)),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                provider = ImageProvider(getIconResByIconId(iconId = iconId)),
                contentDescription = null,
                modifier = GlanceModifier
                    .wrapContentSize()
                    .padding(12.dp),
                //.clickable(actionRunCallback<WeatherRefreshCallback>())

            )
        }
        Text(
            modifier = GlanceModifier
                .wrapContentSize(),
            text = name,
            style = TextStyle(
                fontSize = 24.sp
            )
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                modifier = GlanceModifier
                    .wrapContentSize(),
                text = "$temp${AppStrings.degree}",
                style = TextStyle(
                    fontSize = 36.sp
                )
            )
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                modifier = GlanceModifier
                    .wrapContentSize(),
                text = "↓$min${AppStrings.degree}/$max${AppStrings.degree}↑",
                style = TextStyle(
                    fontSize = 24.sp
                )
            )
        }
    }
}