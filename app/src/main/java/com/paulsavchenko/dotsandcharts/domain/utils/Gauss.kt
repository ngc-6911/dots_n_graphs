package com.paulsavchenko.dotsandcharts.domain.utils

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto


private fun makeTrailingValues(points: List<PointDto>): Array<DoubleArray> {
    val polynomCount = (points.size * 2) - 2
    val polynomLastIndex = polynomCount - 1
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

fun makePolynomialMatrix(points: List<PointDto>): Array<DoubleArray> {
    val polynomCount = (points.size * 2) - 2
    val polynomLastIndex = polynomCount - 1
    val trailingValues = makeTrailingValues(points)

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

fun gauss(matrix: Array<DoubleArray>): List<Pair<PointDto, PointDto>> {
    for(diag in 0..matrix.lastIndex) { // to echelon form
        for (m in 0..matrix.lastIndex) {
            var pivot = matrix[m][diag]
            if (pivot == 0.0) {
                for (m_sub in m..matrix.lastIndex) {
                    if (matrix[m_sub][diag] > 0){
                        matrix.swap(m, m_sub)
                        break
                    }
                }
            }
            pivot = matrix[m][diag]
            if (pivot != 0.0) {
                matrix[m].forEachIndexed { n, mn ->
                    matrix[m][n] = if (m != diag) mn - (matrix[diag][n] * pivot)
                    else mn / pivot
                }
            }
        }
    }
    for(diag in 0..matrix.lastIndex) { // to echelon form reversed
        for (m in 0 until diag) {
            val pivot = matrix[m][diag]
            matrix[m].forEachIndexed { n, mn ->
                matrix[m][n] = mn - (matrix[diag][n] * pivot)
            }
        }
    }
    return List(matrix.size / 2) { idx ->
        val row = idx * 2
        val nextRow = row + 1
        PointDto(
            pointX = matrix[row].let { it[it.lastIndex - 1] },
            pointY = matrix[row].let { it[it.lastIndex] },
        ) to
        PointDto(
            pointX = matrix[nextRow].let { it[it.lastIndex - 1] },
            pointY = matrix[nextRow].let { it[it.lastIndex] },
        )
    }
}

private fun PointDto.toArray(
    multiplier: Double= 1.0
): DoubleArray = doubleArrayOf(pointX * multiplier, pointY * multiplier)


private fun Array<DoubleArray>.swap(from: Int, to: Int) {
    this[from] = this[to].also { this[to] = this[from] }
}
