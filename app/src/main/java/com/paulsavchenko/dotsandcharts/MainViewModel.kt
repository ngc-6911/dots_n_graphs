package com.paulsavchenko.dotsandcharts

import androidx.lifecycle.viewModelScope
import com.paulsavchenko.dotsandcharts.domain.usecase.BuildAdHocBezierUseCase
import com.paulsavchenko.dotsandcharts.domain.usecase.BuildBezierUseCase
import com.paulsavchenko.dotsandcharts.domain.usecase.FetchDotsUseCase
import com.paulsavchenko.dotsandcharts.presentation.base.BaseViewModel
import com.paulsavchenko.dotsandcharts.presentation.base.Event
import com.paulsavchenko.dotsandcharts.presentation.base.State
import com.paulsavchenko.dotsandcharts.presentation.chart.ChartState
import com.paulsavchenko.dotsandcharts.presentation.pointcontrols.ControlsState
import com.paulsavchenko.dotsandcharts.presentation.ui.model.BezierSplineModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.toModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val fetchDotsUseCase: FetchDotsUseCase,
    private val buildBezierUseCase: BuildBezierUseCase,
): BaseViewModel<MainEvents, MainState>(
    defaultState = MainState()
) {

    override fun MainState.reduceState(event: MainEvents) = when(event) {
        MainEvents.RequestDots -> requestDots(controlsState.pointsCount)
        is MainEvents.SetDots -> applyState(
            copy(
                chartState = chartState.copy(
                    rawPoints = event.raw,
                    bezierPoints = event.bezier,
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
                fetchDotsUseCase(FetchDotsUseCase.Params(count)).onSuccess { raw ->
                    buildBezierUseCase(BuildBezierUseCase.Params(raw)).onSuccess { bezier ->
                        applyEvent(
                            MainEvents.SetDots(
                                raw = raw.map { point -> point.toModel() },
                                bezier = bezier.map { spline -> spline.toModel() }
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed interface MainEvents: Event {
    object RequestDots: MainEvents
    class SetDots(val raw: List<PointModel>, val bezier: List<BezierSplineModel>): MainEvents
    class SetCount(val count: Int?): MainEvents
}

data class MainState(
    val chartState: ChartState = ChartState(),
    val controlsState: ControlsState = ControlsState(),
): State