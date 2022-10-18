package com.paulsavchenko.dotsandcharts

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.paulsavchenko.dotsandcharts.data.ApiException
import com.paulsavchenko.dotsandcharts.domain.usecase.CheckStoragePermissionsUseCase
import com.paulsavchenko.dotsandcharts.domain.usecase.FetchDotsUseCase
import com.paulsavchenko.dotsandcharts.domain.usecase.StoreChartUseCase
import com.paulsavchenko.dotsandcharts.presentation.base.BaseViewModel
import com.paulsavchenko.dotsandcharts.presentation.base.Event
import com.paulsavchenko.dotsandcharts.presentation.base.SingleEvent
import com.paulsavchenko.dotsandcharts.presentation.base.State
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartState
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.ControlsState
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.Error
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.toDto
import com.paulsavchenko.dotsandcharts.presentation.ui.model.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val fetchDotsUseCase: FetchDotsUseCase,
    private val checkStoragePermissionsUseCase: CheckStoragePermissionsUseCase,
    private val storeChartUseCase: StoreChartUseCase,
): BaseViewModel<MainEvents, MainState, MainSingleEvents>(
    defaultState = MainState()
) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkStoragePermissionsUseCase().onSuccess { granted ->
                if (!granted) {
                    sendSingleEvent(MainSingleEvents.RequestPermissions)
                }
                applyEvent(MainEvents.SetPermissionsGranted(granted))
            }
        }
    }

    override fun MainState.reduceState(event: MainEvents) = when(event) {
        MainEvents.RequestDots -> requestDots(controlsState.pointsCount)
        is MainEvents.SetDots -> applyState(
            copy(
                controlsState = controlsState.copy(
                    isError = null,
                    canSave = event.points.isNotEmpty(),
                ),
                chartState = chartState.copy(
                    points = event.points,
                )
            )
        )
        is MainEvents.SetCount -> applyState(
            copy(
                controlsState = controlsState.copy(
                    pointsCount = event.count,
                    isError = null,
                )
            )
        )
        is MainEvents.OnError -> applyState(
            copy(
                controlsState = controlsState.copy(
                    isError = event.error,
                    canSave = false,
                ),
                chartState = chartState.copy(
                    points = emptyList(),
                )
            )
        )
        is MainEvents.RequestSave -> requestSave(
            state = chartState,
            lineColor = event.lineColor,
            pointColor = event.pointColor,
        )
        is MainEvents.SetPermissionsGranted -> applyState(
            copy(
                controlsState = controlsState.copy(
                    permissionsGranted = event.isGranted,
                )
            )
        )
    }

    private fun requestSave(
        state: ChartState,
        lineColor: Int,
        pointColor: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            storeChartUseCase(
                StoreChartUseCase.Params(
                    points = state.points.map { it.toDto() },
                    lineColor = lineColor,
                    pointColor = pointColor,
                )
            )
        }
    }

    private fun requestDots(count: Int?) {
        if (count != null) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchDotsUseCase(FetchDotsUseCase.Params(count)).onSuccess { raw ->
                    applyEvent(
                        MainEvents.SetDots(
                            points = raw.map { point -> point.toModel() },
                        )
                    )
                }.onFailure {
                    if (it is ApiException) {
                        it.onBadRequest {
                            applyEvent(MainEvents.OnError(error = Error.Input(it.message ?: "")))
                        }
                        it.onServerError {
                            applyEvent(MainEvents.OnError(error = Error.Server(it.message ?: "")))
                        }
                    }
                }
            }
        }
    }
}

sealed interface MainSingleEvents: SingleEvent {
    object RequestPermissions: MainSingleEvents
}

@Immutable
sealed interface MainEvents: Event {
    object RequestDots: MainEvents
    class RequestSave(val lineColor: Int, val pointColor: Int): MainEvents
    class SetDots(val points: List<PointModel>): MainEvents
    class SetCount(val count: Int?): MainEvents
    class SetPermissionsGranted(val isGranted: Boolean): MainEvents
    class OnError(val error: Error): MainEvents
}

@Immutable
data class MainState(
    val chartState: ChartState = ChartState(),
    val controlsState: ControlsState = ControlsState(),
): State