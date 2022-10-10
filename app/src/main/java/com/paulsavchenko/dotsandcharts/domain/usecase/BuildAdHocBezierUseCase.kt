package com.paulsavchenko.dotsandcharts.domain.usecase

import com.paulsavchenko.dotsandcharts.domain.base.UseCase
import com.paulsavchenko.dotsandcharts.domain.dto.BezierSplineDto
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import com.paulsavchenko.dotsandcharts.domain.utils.gauss
import com.paulsavchenko.dotsandcharts.domain.utils.makePolynomialMatrix
import javax.inject.Inject
import kotlin.math.absoluteValue

class BuildAdHocBezierUseCase @Inject constructor(

): UseCase<BuildAdHocBezierUseCase.Params, List<BezierSplineDto>>() {

    override suspend fun execute(parameters: Params): List<BezierSplineDto> {
        val pointPairs = parameters.points.zipWithNext()
        val minHelper = pointPairs.minOf { (p1, p2) ->
            (p1.pointX.absoluteValue - p2.pointX.absoluteValue).absoluteValue
        }

        return pointPairs.map { (pA, pB) ->
            BezierSplineDto(
                pointA = pA,
                pointB = pB,
                q1 = PointDto(
                    pointX = pA.pointX + minHelper,
                    pointY = pA.pointY,
                ),
                q2 = PointDto(
                    pointX = pB.pointX - minHelper,
                    pointY = pB.pointY,
                ),
            )
        }
    }

    @JvmInline value class Params(val points: List<PointDto>)
}