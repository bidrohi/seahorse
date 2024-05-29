package com.bidyut.tech.seahorse.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SharedPreferencesLocalStore(
    private val context: Context,
) : LocalStore {
    private fun getPreferences(
        languageId: LanguageId,
    ): SharedPreferences {
        return context.getSharedPreferences(
            "seahorse_strings_$languageId",
            Context.MODE_PRIVATE,
        )
    }

    private fun updateLastUpdateTime(
        languageId: LanguageId
    ) {
        getPreferences(languageId).edit {
            putString(LAST_UPDATED_KEY, Clock.System.now().toString())
        }
    }

    override fun getLastUpdatedTime(
        languageId: LanguageId,
    ): Instant? {
        return getPreferences(languageId).getString(LAST_UPDATED_KEY, null)
            ?.let {
                Instant.parse(it)
            }
    }

    override suspend fun replaceStrings(
        languageId: LanguageId,
        strings: Map<String, String>,
    ): Result<Boolean> {
        getPreferences(languageId).edit {
            clear()
            for (entry in strings) {
                putString(entry.key, entry.value)
            }
        }
        updateLastUpdateTime(languageId)
        return Result.success(true)
    }

    override suspend fun clearStore(
        languageId: LanguageId,
    ): Result<Boolean> {
        getPreferences(languageId).edit {
            clear()
        }
        return Result.success(true)
    }

    override fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String? {
        return getPreferences(languageId).getString(key, null)?.let {
            formatString(it, *formatArgs)
        }
    }

    private companion object {
        private const val LAST_UPDATED_KEY = "com_bidyut_tech_seahorse_last_updated"
    }
}
