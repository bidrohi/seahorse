package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString

class MapFallbackSource(
    private val stringMapByLanguage: Map<LanguageId, Map<String, String>>,
) : FallbackSource {
    private var languageId: LanguageId = LanguageEnglish

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
