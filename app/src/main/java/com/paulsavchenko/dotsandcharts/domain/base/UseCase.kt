package com.paulsavchenko.dotsandcharts.domain.base

import android.util.Log

abstract class UseCase<in P, out R> {

    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            Result.success(execute(parameters))
        } catch (ex: Exception) {
            handleError(ex)
        }
    }


    private fun <S> handleError(ex: Throwable): Result<S> {
        Log.e("USE_CASE", ex.stackTraceToString())
        return Result.failure(ex)
    }

    protected abstract suspend fun execute(parameters: P): R
}