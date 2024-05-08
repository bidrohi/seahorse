package com.bidyut.tech.seahorse.example.ui.screen

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

interface StringListContract {
    sealed interface Trigger {
        data class LanguageChanged(
            val languageId: LanguageId,
        ) : Trigger

        data class FetchStringList(
            val languageId: LanguageId,
        ) : Trigger

        data class ClearStore(
            val languageId: LanguageId,
        ) : Trigger

        data class StringClicked(
            val key: String,
        ) : Trigger
    }

    sealed interface UiState {
        data class StatusQuo(
            val languageId: LanguageId,
            val lastUpdatedTime: Instant = Instant.DISTANT_PAST,
        ) : UiState

        data object Connecting : UiState

        data class Error(
            val message: String,
        ) : UiState
    }

    sealed interface Effect {
        data class CopyKeyToClipboard(
            val key: String,
        ) : Effect
    }
}
