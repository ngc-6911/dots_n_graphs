package com.paulsavchenko.dotsandcharts.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<E: Event, S: State>(
    val defaultState: S,
): ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(defaultState)

    val state: Flow<S> get() = _state

    protected abstract fun S.reduceState(event: E)

    protected fun applyState(state: S) {
        _state.tryEmit(state)
    }

    fun applyEvent(event: E) {
        _state.value.reduceState(event)
    }
}

interface Event
interface State