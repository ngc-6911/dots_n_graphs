package com.paulsavchenko.dotsandcharts.domain.base

import android.util.Log

abstract class SimpleUseCase<out R> {

    suspend operator fun invoke(): Result<R> {
        return try {
            Result.success(execute())
        } catch (ex: Exception) {
            handleError(ex)
        }
    }


    private fun <S> handleError(ex: Throwable): Result<S> {
        Log.e("USE_CASE", ex.stackTraceToString())
        return Result.failure(ex)
    }

    protected abstract suspend fun execute(): R
}