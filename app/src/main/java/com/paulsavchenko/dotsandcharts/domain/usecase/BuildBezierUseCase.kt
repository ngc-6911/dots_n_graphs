package com.paulsavchenko.dotsandcharts.domain.usecase

import com.paulsavchenko.dotsandcharts.domain.base.UseCase
import com.paulsavchenko.dotsandcharts.domain.dto.BezierSplineDto
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import com.paulsavchenko.dotsandcharts.domain.utils.gauss
import com.paulsavchenko.dotsandcharts.domain.utils.polynomials.DistanceBasedPolynomialSystem
import javax.inject.Inject

class BuildBezierUseCase @Inject constructor(

): UseCase<BuildBezierUseCase.Params, List<BezierSplineDto>>() {

    override suspend fun execute(parameters: Params): List<BezierSplineDto> {
        return gauss(
            DistanceBasedPolynomialSystem(parameters.points)
        ).let { coeffs ->
            parameters.points.zipWithNext()
                .zip(coeffs) { (a, b), (q1, q2) ->
                    BezierSplineDto(
                        pointA = a,
                        pointB = b,
                        q1 = q1,
                        q2 = q2,
                    )
                }
        }
    }

    @JvmInline value class Params(val points: List<PointDto>)
}