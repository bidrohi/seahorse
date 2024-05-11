package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.JdbcDatabaseDriverFactory

class JdbcSqliteLocalStore : SqliteLocalStore(
    JdbcDatabaseDriverFactory(),
)
