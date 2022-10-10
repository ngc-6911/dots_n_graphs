package com.paulsavchenko.dotsandcharts.presentation.ui.model

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto

data class PointModel(
    val pointX: Float,
    val pointY: Float,
)

fun PointDto.toModel() = PointModel(pointX = pointX.toFloat(), pointY = pointY.toFloat())