package com.paulsavchenko.dotsandcharts.domain.utils.polynomials

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import kotlin.math.hypot
import kotlin.math.pow

class DistanceBasedPolynomialSystem(
    points: List<PointDto>
) : PolynomialSystem(points) {

    val alphaCoeffs = points.zipWithNext().map { (q0, q1) -> // distance-based coeffs
        1.0 / hypot(q0.pointX - q1.pointX, q0.pointY - q1.pointY)
    }

    override fun makeTrailingValues(): Array<DoubleArray> {
        var rowShift = 0
        var alphaShift = 1

        return Array(polynomCount) { row ->
            when(row) {
                0, polynomLastIndex -> points[rowShift].toArray().apply { rowShift += 1 }
                else -> {
                    if (row.mod(2) != 0) {
                        val (x, y) = points[rowShift]
                        val currentA = alphaCoeffs[alphaShift]
                        val prevA = alphaCoeffs[alphaShift - 1]

                        doubleArrayOf(
                            ((currentA.pow(2)) * x) - ((prevA.pow(2)) * x),
                            ((currentA.pow(2)) * y) - ((prevA.pow(2)) * y),
                        )
                    } else {
                        val (x, y) = points[rowShift]
                        val currentA = alphaCoeffs[alphaShift]
                        val prevA = alphaCoeffs[alphaShift - 1]
                        doubleArrayOf(
                            (currentA + prevA) * x,
                            (currentA + prevA) * y,
                        ).also {
                            rowShift += 1
                            alphaShift += 1
                        }
                    }
                }
            }
        }
    }

    override fun makePolynomialMatrix(): Array<DoubleArray> {
        val trailingValues = makeTrailingValues()

        val startRowCoeffs = doubleArrayOf(2.0, -1.0) // start
        val endRowCoeffs = doubleArrayOf(-1.0, 2.0) // end

        var oddRowShift = 0
        var evenRowShift = 1

        var alphaShift = 1
        val coeffValues: Array<DoubleArray> = Array(size = polynomCount) { row ->

            val currentA = alphaCoeffs.getOrElse(alphaShift) { Double.NaN }
            val prevA = alphaCoeffs.getOrElse(alphaShift - 1) { Double.NaN }

            val evenRowCoeffs = doubleArrayOf(prevA, currentA)             // even

            val oddRowCoeffs = doubleArrayOf(   // odd
                prevA.pow(2), -2.0 * prevA.pow(2), 2.0 * currentA.pow(2), -currentA.pow(2)
            )


            DoubleArray(polynomCount) { column ->
                when(row) {
                    0 -> startRowCoeffs.getOrElse(column) { 0.0 } // fill first cells
                    polynomLastIndex -> endRowCoeffs.getOrElse(endRowCoeffs.lastIndex - (polynomLastIndex - column)) { 0.0 } // fill last cells
                    else -> {
                        if (row.mod(2) != 0) {
                            oddRowCoeffs.getOrElse(
                                (column - oddRowShift)
                            ) { 0.0 }
                        } else {
                            evenRowCoeffs.getOrElse(
                                (column - evenRowShift)
                            ) { 0.0 }
                        }
                    }
                }
            }.also {
                if(row != 0 && row != polynomLastIndex) {
                    if (row.mod(2) != 0) oddRowShift += 2 else {
                        evenRowShift += 2
                        alphaShift += 1
                    }
                }
            }
        }
        for (row in 0..polynomLastIndex) {
            coeffValues[row] += trailingValues[row]
        }
        return coeffValues
    }

}