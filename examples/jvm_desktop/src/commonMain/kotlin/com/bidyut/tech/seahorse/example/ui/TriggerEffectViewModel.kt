package com.bidyut.tech.seahorse.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class TriggerEffectViewModel<Trigger, UiState, Effect> : ViewModel() {
    private val initialState: UiState by lazy { makeInitialState() }
    abstract fun makeInitialState(): UiState

    private val viewStateFlow = MutableStateFlow(initialState)
    val viewState = viewStateFlow.asStateFlow()

    private val effectChannel: Channel<Effect> = Channel()
    val effect = effectChannel.receiveAsFlow()

    private val triggerFlow: MutableSharedFlow<Trigger> = MutableSharedFlow()

    init {
        subscribeToTriggers()
    }

    fun sendTrigger(
        trigger: Trigger,
    ) {
        viewModelScope.launch { triggerFlow.emit(trigger) }
    }

    protected fun setState(
        reducer: UiState.() -> UiState,
    ) {
        viewStateFlow.value = viewState.value.reducer()
    }

    private fun subscribeToTriggers() {
        viewModelScope.launch {
            triggerFlow.collect {
                handleTriggers(it)
            }
        }
    }

    protected abstract fun handleTriggers(
        trigger: Trigger,
    )

    protected fun sendEffect(
        builder: () -> Effect,
    ) {
        val effectValue = builder()
        viewModelScope.launch {
            effectChannel.send(effectValue)
        }
    }
}
