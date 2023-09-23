package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

interface LocalSource {
    fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String?
}
