package com.paulsavchenko.dotsandcharts

import androidx.lifecycle.viewModelScope
import com.paulsavchenko.dotsandcharts.domain.usecase.DotsUseCase
import com.paulsavchenko.dotsandcharts.presentation.base.BaseViewModel
import com.paulsavchenko.dotsandcharts.presentation.base.Event
import com.paulsavchenko.dotsandcharts.presentation.base.State
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartState
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.ControlsState
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dotsUseCase: DotsUseCase,
): BaseViewModel<MainEvents, MainState>(
    defaultState = MainState()
) {

    override fun MainState.reduceState(event: MainEvents) = when(event) {
        MainEvents.RequestDots -> requestDots(controlsState.pointsCount)
        is MainEvents.SetDots -> applyState(
            copy(
                chartState = chartState.copy(
                    points = event.dots,
                )
            )
        )
        is MainEvents.SetCount -> applyState(
            copy(
                controlsState = controlsState.copy(
                    pointsCount = event.count,
                )
            )
        )
    }


    private fun requestDots(count: Int?) {
        if (count != null) {
            viewModelScope.launch {
                dotsUseCase(DotsUseCase.Params(count)).onSuccess {
                    applyEvent(
                        MainEvents.SetDots(
                            dots = it.map { point ->
                                PointModel(
                                    pointX = point.pointX,
                                    pointY = point.pointY,
                                )
                            }
                        )
                    )
                }
            }
        }
    }
}

sealed interface MainEvents: Event {
    object RequestDots: MainEvents
    class SetDots(val dots: List<PointModel>): MainEvents
    class SetCount(val count: Int?): MainEvents
}

data class MainState(
    val chartState: ChartState = ChartState(),
    val controlsState: ControlsState = ControlsState(),
): State