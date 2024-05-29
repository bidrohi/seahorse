package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.FallbackSource
import com.bidyut.tech.seahorse.data.LocalStore
import com.bidyut.tech.seahorse.data.NetworkSource
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class SeahorseConfig(
    internal val defaultLanguageId: LanguageId,
    internal val cacheInterval: Duration,
    internal val fallbackSource: FallbackSource,
    internal val localStore: LocalStore?,
    internal val networkSource: NetworkSource?,
) {
    class Builder {
        var defaultLanguageId: LanguageId = LanguageEnglish
        var cacheInterval: Duration = 1.days
        var fallbackSource: FallbackSource? = null
        var localStore: LocalStore? = null
        var networkSource: NetworkSource? = null

        fun build(): SeahorseConfig {
            if (networkSource != null && localStore == null) {
                throw IllegalArgumentException("networkSource use requires localStore to be set")
            }
            return SeahorseConfig(
                defaultLanguageId,
                cacheInterval,
                fallbackSource ?: throw IllegalArgumentException("fallbackSource is not set"),
                localStore,
                networkSource,
            )
        }
    }
}
