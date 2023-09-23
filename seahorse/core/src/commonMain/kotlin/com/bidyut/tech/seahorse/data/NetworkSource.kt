package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

interface NetworkSource {
    suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Map<String, String>>
}
