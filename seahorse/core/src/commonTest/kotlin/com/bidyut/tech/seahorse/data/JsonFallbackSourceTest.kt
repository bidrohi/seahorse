package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JsonFallbackSourceTest {
    @Test
    fun `ensure that fallback string works for all languages`() {
        val source = object: JsonFallbackSource() {
            override fun readStrings(languageId: LanguageId): Map<String, String> {
                return when (languageId) {
                    LanguageEnglish -> mapOf(
                        "key" to "value",
                    )
                    "bn" -> mapOf(
                        "key" to "মান",
                    )
                    else -> parseStrings("{}")
                }
            }

        }
        assertNull(source.getStringByKey("missingKey"))
        assertEquals("value", source.getStringByKey("key"))
        source.setLanguageId("bn")
        assertEquals("মান", source.getStringByKey("key"))
        source.setLanguageId("es")
        assertNull(source.getStringByKey("key"))
    }
}
