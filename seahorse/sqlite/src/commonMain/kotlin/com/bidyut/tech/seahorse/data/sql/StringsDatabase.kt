package com.bidyut.tech.seahorse.data.sql

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@SeahorseInternalApi
internal class StringsDatabase(
    driverFactory: DatabaseDriverFactory,
) {
    private val database by lazy {
        SeahorseStrings(
            driverFactory.createDriver(
                schema = SeahorseStrings.Schema,
                databaseName = "comBidyutTechSeahorseStrings"
            )
        )
    }
    private val dbQuery by lazy {
        database.seahorseStringsQueries
    }
    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    internal suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>,
    ) = withContext(dispatcher) {
        dbQuery.transaction {
            dbQuery.removeAllStringsForLanguage(languageId)
            for ((key, value) in strings) {
                dbQuery.insertString(
                    languageId,
                    key,
                    value,
                )
            }
            dbQuery.insertStringUpdate(
                languageId,
                Clock.System.now().toString()
            )
        }
    }

    internal suspend fun clearStore(
        languageId: LanguageId,
    ) = withContext(dispatcher) {
        dbQuery.transaction {
            dbQuery.removeAllStringsForLanguage(languageId)
            dbQuery.insertStringUpdate(
                languageId,
                Instant.DISTANT_PAST.toString()
            )
        }
    }

    internal fun getStringByKey(
        languageId: LanguageId,
        key: String,
    ): String? = dbQuery.selectStringsByLanguageAndKey(languageId, key)
        .executeAsOneOrNull()
        ?.stringValue

    internal fun getLastUpdatedAt(
        languageId: LanguageId,
    ): Instant? = dbQuery.selectUpdatedAtForLanguage(languageId)
        .executeAsOneOrNull()
        ?.updatedAt?.let {
            Instant.parse(it)
        }
}
