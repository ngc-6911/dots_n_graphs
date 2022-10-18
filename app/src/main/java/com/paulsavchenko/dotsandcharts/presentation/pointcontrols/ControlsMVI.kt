package com.paulsavchenko.dotsandcharts.presentation.pointcontrols

import androidx.compose.runtime.Immutable

@Immutable
data class ControlsState(
    val pointsCount: Int? = null,
    val isError: Error? = null,
    val canSave: Boolean = false,
    val permissionsGranted: Boolean = false,
)

sealed class Error {
    abstract val text: String

    @Immutable
    data class Server(
        override val text: String
    ): Error()

    @Immutable
    data class Input(
        override val text: String
    ): Error()
}