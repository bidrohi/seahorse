package com.bidyut.tech.seahorse.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import com.bidyut.tech.seahorse.model.LanguageId
import java.util.Locale

class ResourceFallbackSource(
    private val appContext: Context,
) : FallbackSource {
    private val configuration = Configuration(appContext.resources.configuration)
    private var context = appContext.createConfigurationContext(configuration)

    @SuppressLint("AppBundleLocaleChanges")
    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        configuration.setLocale(Locale(languageId))
        context = appContext.createConfigurationContext(configuration)
    }

    @SuppressLint("DiscouragedApi")
    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? {
        val sanitisedKey = key.replace("-", "_")
        val stringRes = context.resources.getIdentifier(
            sanitisedKey,
            "string",
            appContext.packageName,
        )
        return if (stringRes != 0) {
            return context.getString(stringRes, *formatArgs)
        } else null
    }
}
