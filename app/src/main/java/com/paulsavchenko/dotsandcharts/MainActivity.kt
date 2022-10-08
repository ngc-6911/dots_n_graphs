package com.paulsavchenko.dotsandcharts

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartComposable
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartState
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.PointControlsComposable
import com.paulsavchenko.dotsandcharts.presentation.pointslist.PointsListComposable
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
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
            isError = false,
            viewEvents = viewEvents
        )
        Divider(thickness = 1.dp)
        ChartComposable(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            points = mainState.chartState.pointsSorted,
        )
        Divider(thickness = 1.dp)
        Box {
            PointsListComposable(
                points = mainState.chartState.pointsSorted,
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
        ChartComposable(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f),
            points = mainState.chartState.pointsSorted,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            PointControlsComposable(
                controlsState = mainState.controlsState,
                viewEvents = viewEvents
            )
            Divider(thickness = 1.dp)
            Box {
                PointsListComposable(
                    points = mainState.chartState.pointsSorted,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreviewPortrait() {
    DotsAndchartsTheme {
        Portrait(
            MainState(
                chartState = ChartState(
                    points = listOf(
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                    ),
                )
            )
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreviewLandscape() {
    DotsAndchartsTheme {
        Landscape(
            MainState(
                chartState = ChartState(
                    points = listOf(
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),
                        PointModel(0.2f, 100.4f),
                        PointModel(-0.422f, 50.2f),
                        PointModel(-98.2f, 1.5f),
                        PointModel(9.2f, 55.5f),
                        PointModel(5.2f, 9.8f),),
                )
            )
        )
    }
}