package com.paulsavchenko.dotsandcharts.data.remote

import com.paulsavchenko.dotsandcharts.data.remote.responses.PointsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("points")
    suspend fun fetchPoints(@Query("count") count: Int): Response<PointsResponse>

    companion object {
        const val API_ROOT = "https://hr-challenge.interactivestandard.com/api/test/"
    }
}