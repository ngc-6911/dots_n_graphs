package com.paulsavchenko.dotsandcharts.presentation.ui.model

import com.paulsavchenko.dotsandcharts.domain.dto.BezierSplineDto

data class BezierSplineModel(
    val pointA: PointModel,
    val pointB: PointModel,
    val q1: PointModel,
    val q2: PointModel,
)

fun BezierSplineDto.toModel() = BezierSplineModel(
    pointA = pointA.toModel(),
    pointB = pointB.toModel(),
    q1 = q1.toModel(),
    q2 = q2.toModel(),
)