package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.DatabaseDriverFactory
import com.bidyut.tech.seahorse.data.sql.StringsDatabase
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant

open class SqliteLocalStore(
    driverFactory: DatabaseDriverFactory,
) : LocalStore {
    private val database by lazy {
        StringsDatabase(driverFactory)
    }

    override fun getLastUpdatedTime(
        languageId: LanguageId,
    ): Instant? {
        return database.getLastUpdatedAt(languageId)
    }

    override suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>
    ): Result<Boolean> {
        return try {
            database.replaceStrings(languageId, strings)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any
    ): String? {
        return database.getStringByKey(languageId, key)
    }
}
