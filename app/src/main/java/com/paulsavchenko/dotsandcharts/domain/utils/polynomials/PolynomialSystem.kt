package com.paulsavchenko.dotsandcharts.domain.utils.polynomials

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto

abstract class PolynomialSystem(
    protected val points: List<PointDto>
) {
    protected val polynomCount get() = (points.size * 2) - 2
    protected val polynomLastIndex get() = polynomCount - 1

    abstract fun makeTrailingValues(): Array<DoubleArray>
    abstract fun makePolynomialMatrix(): Array<DoubleArray>

    companion object {
        fun PointDto.toArray(
            multiplier: Double= 1.0
        ): DoubleArray = doubleArrayOf(pointX * multiplier, pointY * multiplier)

    }
}