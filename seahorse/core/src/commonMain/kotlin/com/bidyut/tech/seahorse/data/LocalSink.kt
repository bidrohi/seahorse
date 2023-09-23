package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

interface LocalSink {
    val lastUpdateTime: Map<LanguageId, Instant>

    suspend fun storeStrings(
        languageId: LanguageId,
        strings: Map<String, String>,
    ): Result<Boolean>
}
