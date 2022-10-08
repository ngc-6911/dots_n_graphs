package com.paulsavchenko.dotsandcharts.data.remote.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PointsResponse(
    @Json(name = "points") var points: List<Point>,
) {

    @JsonClass(generateAdapter = true)
    class Point(
        @Json(name = "x") var x: Float,
        @Json(name = "y") var y: Float,
    )
}