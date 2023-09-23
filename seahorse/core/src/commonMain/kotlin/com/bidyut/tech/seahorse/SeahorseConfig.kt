package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.FallbackSource
import com.bidyut.tech.seahorse.data.LocalSink
import com.bidyut.tech.seahorse.data.LocalSource
import com.bidyut.tech.seahorse.data.NetworkSource
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

data class SeahorseConfig(
    internal var defaultLanguageId: LanguageId,
    internal val cacheInterval: Duration,
    internal val fallbackSource: FallbackSource,
    internal val localSource: LocalSource,
    internal val localSink: LocalSink?,
    internal val networkSource: NetworkSource?,
) {
    class Builder {
        var defaultLanguageId: LanguageId = LanguageEnglish
        var cacheInterval: Duration = 1.days
        var fallbackSource: FallbackSource? = null
        var localSource: LocalSource? = null
        var localSink: LocalSink? = null
        var networkSource: NetworkSource? = null

        fun build() = SeahorseConfig(
            defaultLanguageId,
            cacheInterval,
            fallbackSource ?: throw IllegalStateException("fallbackSource is not set"),
            localSource ?: throw IllegalStateException("localSource is not set"),
            localSink,
            networkSource,
        )
    }
}
