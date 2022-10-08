package com.paulsavchenko.dotsandcharts.presentation.pointcontrols

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.MainEvents
import com.paulsavchenko.dotsandcharts.R
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.DotsAndchartsTheme

@Composable
fun PointControlsComposable(
    controlsState: ControlsState,
    viewEvents: ((MainEvents) -> Unit)? = null,
    isError: Boolean = false,
) {

    var countRemember by remember(controlsState.pointsCount) {
        mutableStateOf(
            controlsState.pointsCount?.toString() ?: ""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = countRemember,
            onValueChange = {
                countRemember = it
                viewEvents?.invoke(MainEvents.SetCount(it.takeIf { it.isNotBlank() }?.toInt()))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = if (isError) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            else ButtonDefaults.buttonColors(),
            onClick = { viewEvents?.invoke(MainEvents.RequestDots) }
        ) {
            Text(
                text = stringResource(
                    id = if (isError) R.string.rerun else R.string.run
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PointsControlsErrorPreview() {
    DotsAndchartsTheme {
        PointControlsComposable(
            ControlsState(
                pointsCount = 100,
                isError = false
            )
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
fun PointsControlsItemPreview() {
    DotsAndchartsTheme {
        PointControlsComposable(
            ControlsState(
                pointsCount = null,
                isError = true
            )
        )
    }
}