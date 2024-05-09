package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId

interface NetworkSource {
    // Strings returned from this source will need to be sanitised
    suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Map<String, String>>
}
