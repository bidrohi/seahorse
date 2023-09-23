package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MapLocalSourceSink : LocalSink, LocalSource {
    private val stringMapByLanguage = mutableMapOf<LanguageId, Map<String, String>>()
    private val _lastUpdateTime = mutableMapOf<LanguageId, Instant>()
    override val lastUpdateTime = _lastUpdateTime

    override suspend fun storeStrings(
        languageId: LanguageId,
        strings: Map<String, String>
    ): Result<Boolean> {
        stringMapByLanguage[languageId] = strings
        lastUpdateTime[languageId] = Clock.System.now()
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
