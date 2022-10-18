package com.paulsavchenko.dotsandcharts.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel<E: Event, S: State, SE: SingleEvent>(
    val defaultState: S,
): ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(defaultState)
    private val _singleEvents = Channel<SE>()

    // Receiving channel as a flow
    val singleEvents = _singleEvents.receiveAsFlow()


    fun sendSingleEvent(singleEvent: SE) {
        viewModelScope.launch {
            _singleEvents.send(singleEvent)
        }
    }

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
interface SingleEvent