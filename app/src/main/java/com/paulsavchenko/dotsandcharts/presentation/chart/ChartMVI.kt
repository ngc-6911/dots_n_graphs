package com.paulsavchenko.dotsandcharts.presentation.chart

import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel

data class ChartState(
    val points: List<PointModel> = emptyList(),
) {
    val pointsSorted by lazy { points.sortedBy { it.pointX } }
}