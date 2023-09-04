package com.lkw1120.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val darkColorPalette = darkColorScheme(
    primary = BlueGray50,
    onPrimary = BlueGray50,
    primaryContainer = BlueGray50,
    onPrimaryContainer = BlueGray50,
    secondary = BlueGray50,
    onSecondary = BlueGray50,
    secondaryContainer = BlueGray50,
    onSecondaryContainer = BlueGray50,
    background = Color.Transparent
)

private val lightColorPalette = lightColorScheme(
    primary = BlueGray900,
    onPrimary = BlueGray900,
    primaryContainer = BlueGray900,
    onPrimaryContainer = BlueGray900,
    secondary = BlueGray800,
    onSecondary = BlueGray800,
    secondaryContainer = BlueGray800,
    onSecondaryContainer = BlueGray800,
    background = Color.Transparent


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun WeatherAppTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Black)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

