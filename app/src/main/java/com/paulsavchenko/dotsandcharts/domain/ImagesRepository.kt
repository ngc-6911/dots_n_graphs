package com.paulsavchenko.dotsandcharts.domain

import com.paulsavchenko.dotsandcharts.data.ImagesDatasource
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto

interface ImagesRepository {

    suspend fun storeChart(points: List<PointDto>, pointColor: Int, lineColor: Int)

    class Impl(
        private val imagesDatasource: ImagesDatasource,
    ): ImagesRepository {

        override suspend fun storeChart(points: List<PointDto>, pointColor: Int, lineColor: Int) {
            imagesDatasource.storeChart(points, pointColor, lineColor)
        }
    }
}