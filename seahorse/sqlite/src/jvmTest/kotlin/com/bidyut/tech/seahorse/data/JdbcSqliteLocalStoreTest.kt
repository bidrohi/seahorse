package com.bidyut.tech.seahorse.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JdbcSqliteLocalStoreTest {
    private val store = JdbcSqliteLocalStore {
        JdbcSqliteDriver.IN_MEMORY
    }

    @AfterTest
    fun breakdown() {
        store.clear("en", "bn")
    }

    @Test
    fun `ensure we can add strings works for all languages`() {
        assertNull(store.getStringByKey("en", "key"))
        val result = runBlocking {
            store.replaceStrings(
                "en",
                mapOf(
                    "key" to "value",
                ),
            )
        }
        assertTrue(result.isSuccess)
        assertNotNull(store.getLastUpdatedTime("en"))
        assertEquals("value", store.getStringByKey("en", "key"))
        assertNull(store.getStringByKey("en", "missingKey"))
        assertNull(store.getStringByKey("bn", "key"))
        assertNull(store.getLastUpdatedTime("bn"))
    }

    @Test
    fun `ensure we actually replace strings`() {
        assertNull(store.getStringByKey("en", "key"))
        runBlocking {
            store.replaceStrings(
                "en",
                mapOf(
                    "key" to "value",
                ),
            )
        }
        assertEquals("value", store.getStringByKey("en", "key"))
        runBlocking {
            store.replaceStrings(
                "en",
                mapOf(
                    "key" to "value2",
                ),
            )
        }
        assertEquals("value2", store.getStringByKey("en", "key"))
    }
}
