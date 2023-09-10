package com.lkw1120.weatherapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.theme.LightBlue700

@Composable
fun ErrorDialog(
    errorContent: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Surface(
            modifier = Modifier
                .width(360.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) { errorContent() }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRefresh: () -> Unit,
    exit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp),
            text = AppStrings.error_title,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
            text = message,
            fontSize = 18.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue700,
                    contentColor = Color.White
                ),
                onClick = { onRefresh() }

            ) {
                Text(
                    text = "Refresh",
                    fontSize = 18.sp
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue700,
                    contentColor = Color.White
                ),
                onClick = { exit() }
            ) {
                Text(
                    text = "Exit",
                    fontSize = 18.sp
                )
            }
        }
    }
}