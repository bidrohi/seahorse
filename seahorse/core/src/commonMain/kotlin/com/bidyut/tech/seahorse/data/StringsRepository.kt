package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId

class StringsRepository(
    private val source: LocalSource,
    private val fallback: FallbackSource,
) {
    fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String = source.getStringByKey(languageId, key, *formatArgs)
        ?: fallback.getStringByKey(key, *formatArgs)
        ?: key
}
