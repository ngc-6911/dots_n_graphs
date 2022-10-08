package com.paulsavchenko.dotsandcharts.domain.usecase

import com.paulsavchenko.dotsandcharts.domain.DotsRepository
import com.paulsavchenko.dotsandcharts.domain.base.UseCase
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import javax.inject.Inject

class DotsUseCase @Inject constructor(
    private val dotsRepository: DotsRepository,
): UseCase<DotsUseCase.Params, List<PointDto>>() {

    override suspend fun execute(parameters: Params): List<PointDto> {
        return dotsRepository.getDots(parameters.count)
    }

    @JvmInline value class Params(val count: Int)
}