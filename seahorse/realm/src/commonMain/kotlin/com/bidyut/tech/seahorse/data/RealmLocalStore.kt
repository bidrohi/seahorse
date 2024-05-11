package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.realm.StringEntity
import com.bidyut.tech.seahorse.data.realm.StringUpdateEntity
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import com.bidyut.tech.seahorse.utils.sanitiseFormatString
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.Instant

class RealmLocalStore(
    realmConfig: RealmConfiguration.Builder.() -> Unit = {
        deleteRealmIfMigrationNeeded()
    },
) : LocalStore {
    private val realm by lazy {
        val configuration = RealmConfiguration.Builder(
            schema = setOf(
                StringEntity::class,
                StringUpdateEntity::class,
            )
        ).apply {
            name("comBidyutTechSeahorseStrings.str.realm")
            realmConfig()
        }.build()
        Realm.open(configuration)
    }

    override fun getLastUpdatedTime(
        languageId: LanguageId,
    ): Instant? {
        return realm.query<StringUpdateEntity>("languageId = $0", languageId)
            .first()
            .find()
            ?.updatedAt
            ?.let {
                Instant.fromEpochSeconds(it.epochSeconds)
            }
    }

    override suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>
    ): Result<Boolean> {
        return try {
            realm.writeBlocking {
                delete(query<StringEntity>("languageId = $0", languageId))
                for ((key, value) in strings) {
                    copyToRealm(StringEntity().apply {
                        this.languageId = languageId
                        this.key = key
                        this.stringValue = sanitiseFormatString(value)
                    })
                }
                val updateEntity = query<StringUpdateEntity>("languageId = $0", languageId)
                    .first()
                    .find()
                    ?: StringUpdateEntity().apply {
                        this.languageId = languageId
                    }
                updateEntity.updatedAt = RealmInstant.now()
                copyToRealm(updateEntity)
            }
            Result.success(true)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        }
    }

    override suspend fun clearStore(
        languageId: LanguageId,
    ): Result<Boolean> {
        return try {
            realm.writeBlocking {
                delete(query<StringUpdateEntity>("languageId = $0", languageId))
                delete(query<StringEntity>("languageId = $0", languageId))
            }
            Result.success(true)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        }
    }

    override fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any
    ): String? {
        return realm.query<StringEntity>("languageId = $0 AND key = $1", languageId, key)
            .first()
            .find()
            ?.stringValue
            ?.let {
                formatString(it, *formatArgs)
            }
    }
}
