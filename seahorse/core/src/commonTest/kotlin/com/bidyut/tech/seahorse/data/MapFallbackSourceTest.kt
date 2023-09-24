package com.bidyut.tech.seahorse.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapFallbackSourceTest {
    @Test
    fun `ensure that fallback string works for all languages`() {
        val source = MapFallbackSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                ),
                "bn" to mapOf(
                    "key" to "মান",
                ),
            ),
        )
        assertNull(source.getStringByKey("missingKey"))
        assertEquals("value", source.getStringByKey("key"))
        source.setLanguageId("bn")
        assertEquals("মান", source.getStringByKey("key"))
        source.setLanguageId("es")
        assertNull(source.getStringByKey("key"))
    }
}
