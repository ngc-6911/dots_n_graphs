package com.paulsavchenko.dotsandcharts.domain.utils.polynomials

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto

class SimplePolynomialSystem(
    points: List<PointDto>
): PolynomialSystem(points) {

    override fun makeTrailingValues(): Array<DoubleArray> {
        var rowShift = 0
        return Array(polynomCount) { row ->
            when(row) {
                0, polynomLastIndex -> points[rowShift].toArray().apply { rowShift += 1 }
                else -> {
                    if (row.mod(2) != 0) {
                        doubleArrayOf(0.0, 0.0)
                    } else {
                        points[rowShift].toArray(2.0).apply { rowShift += 1 }
                    }
                }
            }
        }
    }

    override fun makePolynomialMatrix(): Array<DoubleArray> {
        val trailingValues = makeTrailingValues()

        val startRowCoeffs = doubleArrayOf(2.0, -1.0) // start
        val endRowCoeffs = doubleArrayOf(-1.0, 2.0) // end

        val evenRowCoeffs = doubleArrayOf(1.0, 1.0)          // even
        val oddRowCoeffs = doubleArrayOf(1.0, -2.0, 2.0, -1.0) // odd
        var oddRowShift = 0
        var evenRowShift = 1
        val coeffValues: Array<DoubleArray> = Array(size = polynomCount) { row ->
            DoubleArray(polynomCount) { column ->
                when(row) {
                    0 -> startRowCoeffs.getOrElse(column) { 0.0} // fill first cells
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
                    if (row.mod(2) != 0) oddRowShift += 2 else evenRowShift += 2
                }
            }
        }
        for (row in 0..polynomLastIndex) {
            coeffValues[row] += trailingValues[row]
        }
        return coeffValues
    }


}