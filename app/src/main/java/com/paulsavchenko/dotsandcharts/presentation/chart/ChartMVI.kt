package com.paulsavchenko.dotsandcharts.presentation.chart

import androidx.compose.runtime.Immutable
import com.paulsavchenko.dotsandcharts.presentation.ui.model.BezierSplineModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel

@Immutable
data class ChartState(
    val useBezier: Boolean = true,
    val bezierPoints: List<BezierSplineModel> = emptyList(),
    val rawPoints: List<PointModel> = emptyList(),
) {
    val bezierUnpacked: Set<PointModel> by lazy {
        rawPoints.toMutableSet().apply {
            bezierPoints.forEach { add(it.q1); add(it.q2) }
        }
    }
}