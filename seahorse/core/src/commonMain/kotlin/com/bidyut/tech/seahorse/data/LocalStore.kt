package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.runBlocking
import kotlin.time.Instant

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

    fun clear(
        vararg languageIds: LanguageId,
    ) {
        runBlocking {
            for (languageId in languageIds) {
                clearStore(languageId)
            }
        }
    }
}
