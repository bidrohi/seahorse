package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ByteArrayFallbackSourceTest {
    @Test
    fun `ensure that fallback string works for all languages`() {
        val source = ByteArrayFallbackSource(
            toByteArray = {
                when (it) {
                    LanguageEnglish -> """
                        {"key":"value"}
                    """.trimIndent().encodeToByteArray()

                    LanguageBengali -> """
                        {"key":"মান"}
                    """.trimIndent().encodeToByteArray()

                    else -> "{}".encodeToByteArray()
                }
            }
        )
        assertNull(source.getStringByKey("missingKey"))
        assertEquals("value", source.getStringByKey("key"))
        source.setLanguageId("bn")
        assertEquals("মান", source.getStringByKey("key"))
        source.setLanguageId("es")
        assertNull(source.getStringByKey("key"))
    }
}
