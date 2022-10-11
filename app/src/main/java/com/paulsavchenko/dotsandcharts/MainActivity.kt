package com.paulsavchenko.dotsandcharts

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartComposable
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.PointControlsComposable
import com.paulsavchenko.dotsandcharts.presentation.pointslist.PointsListComposable
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.DotsAndchartsTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by vmDelegate {
        (application as DotsApp).dagger.mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val orientation = LocalConfiguration.current.orientation
            DotsAndchartsTheme {
                // A surface container using the 'background' color from the theme
                val st by mainViewModel.state.collectAsState(initial = mainViewModel.defaultState)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) Landscape(st, mainViewModel::applyEvent)
                    else Portrait(st, mainViewModel::applyEvent)
                }
            }
        }
    }
}

@Composable
fun Portrait(
    mainState: MainState,
    viewEvents: ((MainEvents) -> Unit)? = null,
) {
    Column {
        PointControlsComposable(
            controlsState = mainState.controlsState,
            viewEvents = viewEvents
        )
        Divider(thickness = 1.dp)
        ChartComposable(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            chartState = mainState.chartState,
        )
        Divider(thickness = 1.dp)
        Box {
            PointsListComposable(
                points = mainState.chartState.rawPoints,
            )
        }
    }
}

@Composable
fun Landscape(
    mainState: MainState,
    viewEvents: ((MainEvents) -> Unit)? = null,
) {
    Row {
        Box(modifier = Modifier.fillMaxSize().weight(3f)) {
            ChartComposable(
                chartState = mainState.chartState,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            PointControlsComposable(
                controlsState = mainState.controlsState,
                viewEvents = viewEvents
            )
            Divider(thickness = 1.dp)
            Box {
                PointsListComposable(
                    points = mainState.chartState.rawPoints,
                )
            }
        }
    }
}
