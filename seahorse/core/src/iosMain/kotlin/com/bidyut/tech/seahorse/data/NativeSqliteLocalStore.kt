package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.DatabaseDriverFactory

class NativeSqliteLocalStore : SqliteLocalStore(
    DatabaseDriverFactory(),
) {
    override fun sanitiseString(string: String) = string.replace("%s", "%@")
}
