package com.paulsavchenko.dotsandcharts.domain

import com.paulsavchenko.dotsandcharts.data.DotsDatasource
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto

interface DotsRepository {

    suspend fun getDots(count: Int): List<PointDto>

    class Impl(
        private val dotsDatasource: DotsDatasource,
    ): DotsRepository {
        override suspend fun getDots(count: Int): List<PointDto> {
            return dotsDatasource.getDots(count)
        }

    }
}