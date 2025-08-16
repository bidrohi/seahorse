package com.bidyut.tech.seahorse.example.ui.screen

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.example.di.AppGraph
import com.bidyut.tech.seahorse.example.ui.TriggerEffectViewModel
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Instant

class StringListViewModel(
    val seahorse: Seahorse,
    val stringKeys: List<String>,
) : TriggerEffectViewModel<
        StringListContract.Trigger,
        StringListContract.UiState,
        StringListContract.Effect,
        >() {
    override fun makeInitialState(): StringListContract.UiState =
        StringListContract.UiState.StatusQuo(LanguageEnglish)

    override fun handleTriggers(
        trigger: StringListContract.Trigger,
    ) {
        when (trigger) {
            is StringListContract.Trigger.LanguageChanged -> changeLanguage(trigger.languageId)
            is StringListContract.Trigger.FetchStringList -> fetchStrings(trigger.languageId)
            is StringListContract.Trigger.ClearStore -> clearStore(trigger.languageId)
            is StringListContract.Trigger.StringClicked -> copyKeyToClipboard(trigger.key)
        }
    }

    private fun changeLanguage(
        languageId: LanguageId,
    ) {
        viewModelScope.launch {
            seahorse.defaultLanguageId = languageId
            setState {
                StringListContract.UiState.StatusQuo(
                    languageId = languageId,
                    lastUpdatedTime = (this as? StringListContract.UiState.StatusQuo)?.lastUpdatedTime ?: Instant.DISTANT_PAST,
                )
            }
        }
    }

    private fun fetchStrings(
        languageId: LanguageId,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                StringListContract.UiState.Connecting
            }
            seahorse.fetchStrings(languageId).fold(
                onSuccess = {
                    setState {
                        StringListContract.UiState.StatusQuo(
                            languageId = languageId,
                            lastUpdatedTime = it,
                        )
                    }
                },
                onFailure = { error ->
                    setState {
                        StringListContract.UiState.Error(
                            message = error.message ?: "Unknown error on fetch",
                        )
                    }
                },
            )
        }
    }

    private fun clearStore(
        languageId: LanguageId,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                StringListContract.UiState.Connecting
            }
            seahorse.clearStore(languageId).fold(
                onSuccess = {
                    setState {
                        StringListContract.UiState.StatusQuo(
                            languageId = languageId,
                            lastUpdatedTime = Instant.DISTANT_PAST,
                        )
                    }
                },
                onFailure = { error ->
                    setState {
                        StringListContract.UiState.Error(
                            message = error.message ?: "Unknown error on clear",
                        )
                    }
                },
            )
        }
    }

    private fun copyKeyToClipboard(
        key: String,
    ) {
        sendEffect {
            StringListContract.Effect.CopyKeyToClipboard(key)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                StringListViewModel(
                    seahorse = AppGraph.instance.seahorse,
                    stringKeys = AppGraph.instance.stringKeys,
                )
            }
        }
    }
}
