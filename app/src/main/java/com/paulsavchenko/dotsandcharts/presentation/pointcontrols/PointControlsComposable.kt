package com.paulsavchenko.dotsandcharts.presentation.pointcontrols

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.MainEvents
import com.paulsavchenko.dotsandcharts.R
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartState
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.DotsAndchartsTheme
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.InfoBlue
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.InfoOrange
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.InfoRed

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PointControlsComposable(
    controlsState: ControlsState,
    viewEvents: ((MainEvents) -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val infoTextStr = stringResource(id = R.string.info_text)

    val chartColor = MaterialTheme.colors.primary.toArgb()
    val pointColor = MaterialTheme.colors.primaryVariant.toArgb()

    val ignore = Regex("\\D")
    val infoText = remember(controlsState.isError) {
        when(controlsState.isError) {
            is Error.Input, is Error.Server -> controlsState.isError.text
            null -> infoTextStr
        }
    }
    val cardStateColor = remember(controlsState.isError) {
        when(controlsState.isError) {
            is Error.Input -> InfoRed
            is Error.Server -> InfoOrange
            null -> InfoBlue
        }
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, color = cardStateColor)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = when(controlsState.isError) {
                            is Error.Input -> R.drawable.ic_error_24
                            is Error.Server -> R.drawable.ic_refresh_24
                            null -> R.drawable.ic_info_24
                        }
                    ),
                    tint = cardStateColor,
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    text = infoText
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = controlsState.pointsCount?.toString() ?: "",
            onValueChange = {
                if (!it.contains(ignore)) viewEvents?.invoke(MainEvents.SetCount(it.takeIf { it.isNotBlank() }?.toInt()))
            },
            keyboardActions = KeyboardActions(onDone = {
                viewEvents?.invoke(MainEvents.RequestDots)
                keyboardController?.hide()
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClick = { viewEvents?.invoke(MainEvents.RequestDots); keyboardController?.hide() }
            ) {
                Text(
                    text = stringResource(
                        id = if (controlsState.isError is Error.Server) R.string.rerun else R.string.run
                    )
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClick = { viewEvents?.invoke(
                    MainEvents.RequestSave(
                        lineColor = chartColor,
                        pointColor = pointColor,
                    )
                ); keyboardController?.hide() },
                enabled = controlsState.isError == null && controlsState.canSave && controlsState.permissionsGranted,
            ) {
                Text(
                    text = stringResource(
                        id = R.string.save
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PointsControlsPreview() {
    DotsAndchartsTheme {
        PointControlsComposable(
            ControlsState(
                pointsCount = 100,
                isError = null,
            ),
        )
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
fun PointsControlsInputErrorPreview() {
    DotsAndchartsTheme {
        PointControlsComposable(
            ControlsState(
                pointsCount = null,
                isError = Error.Input("Input error"),
                canSave = false,
            ),
        )
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
fun PointsControlsServerPreview() {
    DotsAndchartsTheme {
        PointControlsComposable(
            ControlsState(
                pointsCount = null,
                isError = Error.Server("Server error"),
                canSave = false,
            ),
        )
    }
}