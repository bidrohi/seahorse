package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.DatabaseDriverFactory

class NativeSqliteLocalStore : SqliteLocalStore(
    DatabaseDriverFactory(),
) {
    private val paramMatcher = Regex("(?<!%)%([1-9]\\$|)s")

    override fun sanitiseString(string: String) = string.replace(paramMatcher) {
        it.value.replace("s", "@")
    }
}
