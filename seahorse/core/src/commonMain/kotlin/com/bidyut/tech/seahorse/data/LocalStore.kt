package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

interface LocalStore {
    fun getLastUpdatedTime(
        languageId: LanguageId
    ): Instant?

    suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>,
    ): Result<Boolean>

    suspend fun clearStore(
        languageId: LanguageId,
    ): Result<Boolean>

    fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String?
}
