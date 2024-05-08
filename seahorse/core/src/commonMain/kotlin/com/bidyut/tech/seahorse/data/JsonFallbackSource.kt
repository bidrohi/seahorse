package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString

abstract class JsonFallbackSource : FallbackSource {
    private var languageId: LanguageId = LanguageEnglish
    private val stringMapByLanguage = mutableMapOf<LanguageId, Map<String, String>>()

    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        if (!stringMapByLanguage.contains(languageId)) {
            stringMapByLanguage[languageId] = fetchStrings(languageId)
        }
        this.languageId = languageId
    }

    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? = stringMapByLanguage[languageId]?.get(key)?.let {
        formatString(it, *formatArgs)
    }

    abstract fun fetchStrings(
        languageId: LanguageId,
    ): Map<String, String>
}
