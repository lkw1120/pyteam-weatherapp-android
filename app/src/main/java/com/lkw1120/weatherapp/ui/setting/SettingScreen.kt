package com.lkw1120.weatherapp.ui.setting

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.lkw1120.weatherapp.common.AppStrings

@Composable
fun SettingsScreen(
    settingViewModel: SettingViewModel,
    onNavigateToHomeScreen: () -> Unit
) {

    val currentState by settingViewModel.settingScreenState.collectAsState()
    val activity = (LocalContext.current as? Activity)

    settingViewModel.getSettings()

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = {
            SettingAppBarSection(onNavigateToHomeScreen)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            SettingContent(currentState) { units ->
                settingViewModel.updateUnits(units)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingAppBarSection(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .statusBarsPadding(),
        colors = TopAppBarDefaults
            .topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(
                text = AppStrings.setting_title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
        },
        navigationIcon = {
            /*
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,

                )
            }

             */
        }
    )
}

@Composable
fun SettingContent(
    currentState: SettingScreenState,
    onClick: (units: String) -> Unit
) {
    when (currentState) {
        is SettingScreenState.Loading -> {

        }

        is SettingScreenState.Success -> {
            if (currentState.settings != null) {
                val settings = currentState.settings
                SettingSection(
                    modifier = Modifier,
                    settings = settings,
                    onClick = onClick
                )
            }
        }

        is SettingScreenState.Error -> {

        }
    }
}

@Composable
fun SettingSection(
    modifier: Modifier = Modifier,
    settings: Map<String, String>,
    onClick: (units: String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val activityLauncher = rememberLauncherForActivityResult(StartActivityForResult()) {}

    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(bounded = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .indication(interactionSource, ripple)
                .clickable {
                    onClick(settings["UNITS"].toString())
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = AppStrings.change_unit
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = if (settings["UNITS"].toString() == "metric") {
                        "Celsius/meter"
                    } else {
                        "Fahrenheit/mile"
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(18.dp, 0.dp)
                .background(Color.LightGray)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .indication(interactionSource, ripple)
                .clickable {
                    activityLauncher.launch(Intent(context, OssLicensesMenuActivity::class.java))
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "OSS Licenses"
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(18.dp, 0.dp)
                .background(Color.LightGray)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {

        }
    }
}
