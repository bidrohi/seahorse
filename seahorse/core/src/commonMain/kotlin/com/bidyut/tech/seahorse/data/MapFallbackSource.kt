package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import com.bidyut.tech.seahorse.utils.sanitiseFormatString

class MapFallbackSource(
    strings: Map<LanguageId, Map<String, String>>,
) : FallbackSource {
    private var languageId: LanguageId = LanguageEnglish
    private val stringMapByLanguage: Map<LanguageId, Map<String, String>> = strings.mapValues { m ->
        m.value.mapValues {
            sanitiseFormatString(it.value)
        }
    }

    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        this.languageId = languageId
    }

    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? = stringMapByLanguage[languageId]?.get(key)?.let {
        formatString(it, *formatArgs)
    }
}
