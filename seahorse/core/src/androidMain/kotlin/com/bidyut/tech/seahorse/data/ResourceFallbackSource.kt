package com.bidyut.tech.seahorse.data

import android.content.Context
import android.content.res.Configuration
import com.bidyut.tech.seahorse.model.LanguageId
import java.util.Locale

class ResourceFallbackSource(
    private val appContext: Context,
) : FallbackSource {
    private val configuration = Configuration(appContext.resources.configuration)
    private var context = appContext.createConfigurationContext(configuration)

    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        configuration.setLocale(Locale(languageId))
        context = appContext.createConfigurationContext(configuration)
    }

    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? {
        val stringRes = context.resources.getIdentifier(key, "string", appContext.packageName)
        return if (stringRes != 0) {
            return context.getString(stringRes, *formatArgs)
        } else null
    }
}
