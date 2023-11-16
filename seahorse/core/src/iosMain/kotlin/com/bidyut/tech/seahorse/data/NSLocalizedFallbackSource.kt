package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import platform.Foundation.NSBundle

class NSLocalizedFallbackSource(
    private val rootBundle: NSBundle = NSBundle.mainBundle,
    private val tableName: String? = null,
    private val getPathForLanguageId: (LanguageId) -> String = { it },
) : FallbackSource {
    private var bundle = rootBundle

    constructor() : this(NSBundle.mainBundle)

    constructor(
        rootBundle: NSBundle,
    ) : this(rootBundle, null)

    constructor(
        rootBundle: NSBundle,
        tableName: String? = null,
    ) : this(rootBundle, tableName, { it })

    override fun setLanguageId(
        languageId: LanguageId,
    ) {
        rootBundle.pathForResource(getPathForLanguageId(languageId), "lproj")?.let { path ->
            NSBundle.bundleWithPath(path)?.let {
                bundle = it
            }
        }
    }

    override fun getStringByKey(
        key: String,
        vararg formatArgs: Any,
    ): String? {
        val fmt = bundle.localizedStringForKey(key, null, tableName)
        return if (fmt.isNotEmpty()) {
            formatString(fmt, *formatArgs)
        } else null
    }
}
