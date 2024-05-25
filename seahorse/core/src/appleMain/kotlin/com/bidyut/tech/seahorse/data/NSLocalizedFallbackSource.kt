package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.formatString
import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
class NSLocalizedFallbackSource(
    @ObjCName("_")
    private val rootBundle: NSBundle,
    private val tableName: String?,
    private val getPathForLanguageId: (LanguageId) -> String,
) : FallbackSource {
    private var bundle = rootBundle

    constructor() : this(NSBundle.mainBundle)

    constructor(
        @ObjCName("_")
        rootBundle: NSBundle,
    ) : this(rootBundle, null)

    constructor(
        @ObjCName("_")
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
