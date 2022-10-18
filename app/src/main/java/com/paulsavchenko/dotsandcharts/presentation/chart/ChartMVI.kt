package com.paulsavchenko.dotsandcharts.presentation.chart

import androidx.compose.runtime.Immutable
import com.paulsavchenko.dotsandcharts.presentation.ui.model.BezierSplineModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel

@Immutable
data class ChartState(
    val points: List<PointModel> = emptyList(),
)