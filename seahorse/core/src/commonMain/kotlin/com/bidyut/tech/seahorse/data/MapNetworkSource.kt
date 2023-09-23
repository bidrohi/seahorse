package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId

class MapNetworkSource(
    private val stringMapByLanguage: Map<LanguageId, Map<String, String>>,
) : NetworkSource {
    override suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Map<String, String>> {
        val result = stringMapByLanguage[languageId]
        return if (result != null) {
            Result.success(result)
        } else {
            Result.failure(IllegalStateException("No strings found for languageId=$languageId"))
        }
    }
}
