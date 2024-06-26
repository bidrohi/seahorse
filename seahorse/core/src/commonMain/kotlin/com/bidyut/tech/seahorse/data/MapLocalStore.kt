package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import com.bidyut.tech.seahorse.utils.sanitiseFormatString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MapLocalStore : LocalStore {
    private val stringMapByLanguage = mutableMapOf<LanguageId, Map<String, String>>()
    private val lastUpdatedTime = mutableMapOf<LanguageId, Instant>()

    override fun getLastUpdatedTime(
        languageId: LanguageId
    ): Instant? {
        return lastUpdatedTime[languageId]
    }

    override suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>
    ): Result<Boolean> {
        stringMapByLanguage[languageId] = strings.mapValues {
            sanitiseFormatString(it.value)
        }
        lastUpdatedTime[languageId] = Clock.System.now()
        return Result.success(true)
    }

    override suspend fun clearStore(languageId: LanguageId): Result<Boolean> {
        stringMapByLanguage.remove(languageId)
        lastUpdatedTime.remove(languageId)
        return Result.success(true)
    }

    override fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any
    ): String? = stringMapByLanguage[languageId]?.get(key)?.let {
        formatString(it, *formatArgs)
    }
}
