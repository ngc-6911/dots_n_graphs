package com.paulsavchenko.dotsandcharts.data

import com.paulsavchenko.dotsandcharts.data.remote.API
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import java.io.IOException

interface DotsDatasource {

    suspend fun getDots(count: Int): List<PointDto>

    class Impl(
        private val api: API,
    ): DotsDatasource {
        override suspend fun getDots(count: Int): List<PointDto> {
            val result = api.fetchPoints(count)
            val body = result.body()
            return if (result.isSuccessful && body != null) body.points.map { PointDto(pointX = it.x.toDouble(), pointY = it.y.toDouble()) }
            else throw ApiException(
                code = result.code(),
                message = result.errorBody()?.string() ?: "unknown"
            )
        }

    }
}

class ApiException(
    val code: Int,
    message: String
): IOException(message) {
    inline fun onBadRequest(block: () -> Unit) {
        if (code == 400) block()
    }
    inline fun onServerError(block: () -> Unit) {
        if (code == 500) block()
    }
}
