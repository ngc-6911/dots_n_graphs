package com.paulsavchenko.dotsandcharts.domain.dto

data class BezierSplineDto(
    val pointA: PointDto,
    val pointB: PointDto,
    val q1: PointDto,
    val q2: PointDto,
)