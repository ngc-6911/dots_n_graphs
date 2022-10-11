package com.paulsavchenko.dotsandcharts.domain.utils

import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import com.paulsavchenko.dotsandcharts.domain.utils.polynomials.PolynomialSystem


fun gauss(system: PolynomialSystem): List<Pair<PointDto, PointDto>> {
    val matrix = system.makePolynomialMatrix()
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
private fun Array<DoubleArray>.swap(from: Int, to: Int) {
    this[from] = this[to].also { this[to] = this[from] }
}
