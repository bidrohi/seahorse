package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.NativeDatabaseDriverFactory

class NativeSqliteLocalStore : SqliteLocalStore(
    NativeDatabaseDriverFactory(),
)
