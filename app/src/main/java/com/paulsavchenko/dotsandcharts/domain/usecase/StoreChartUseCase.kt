package com.paulsavchenko.dotsandcharts.domain.usecase

import com.paulsavchenko.dotsandcharts.domain.ImagesRepository
import com.paulsavchenko.dotsandcharts.domain.base.UseCase
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import javax.inject.Inject

class StoreChartUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
): UseCase<StoreChartUseCase.Params, Unit>() {

    override suspend fun execute(parameters: Params) {
        with(parameters) {
            imagesRepository.storeChart(
                points,
                pointColor,
                lineColor
            )
        }
    }

    data class Params(
        val points: List<PointDto>,
        val pointColor: Int,
        val lineColor: Int,
    )
}