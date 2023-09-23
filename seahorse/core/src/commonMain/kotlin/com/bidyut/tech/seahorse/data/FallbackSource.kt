package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId

interface FallbackSource {
    fun setLanguageId(
        languageId: LanguageId,
    )

    fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String?
}
