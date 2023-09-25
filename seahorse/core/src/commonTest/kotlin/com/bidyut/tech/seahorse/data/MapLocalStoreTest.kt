package com.bidyut.tech.seahorse.data

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapLocalStoreTest {
    @Test
    fun `ensure we can add strings works for all languages`() {
        val store = MapLocalStore()
        assertNull(store.getStringByKey("en", "key"))
        runBlocking {
            store.storeStrings(
                "en",
                mapOf(
                    "key" to "value",
                ),
            )
        }
        assertEquals("value", store.getStringByKey("en", "key"))
        assertNull(store.getStringByKey("en", "missingKey"))
        assertNull(store.getStringByKey("bn", "key"))
    }
}
