package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import com.bidyut.tech.seahorse.utils.sanitiseFormatString
import kotlinx.serialization.json.Json

abstract class JsonFallbackSource(
    protected val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) : FallbackSource {
    private var languageId: LanguageId = LanguageEnglish
    private var currentStringsLoaded = false
    private val stringMapByLanguage = mutableMapOf<LanguageId, Map<String, String>>()

    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        if (!stringMapByLanguage.contains(languageId)) {
            stringMapByLanguage[languageId] = readStrings(languageId).mapValues {
                sanitiseFormatString(it.value)
            }
            currentStringsLoaded = true
        }
        this.languageId = languageId
    }

    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? {
        if (!currentStringsLoaded) {
            setLanguageId(languageId)
        }
        return stringMapByLanguage[languageId]?.get(key)?.let {
            formatString(it, *formatArgs)
        }
    }

    protected fun parseStrings(
        jsonBody: String,
    ): Map<String, String> = json.decodeFromString<Map<String, String>>(jsonBody)

    abstract fun readStrings(
        languageId: LanguageId,
    ): Map<String, String>
}
