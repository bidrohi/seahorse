package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.data.sql.JdbcDatabaseDriverFactory

class JdbcSqliteLocalStore(
    urlBuilder: (String) -> String = { "jdbc:sqlite:$it.db" },
) : SqliteLocalStore(
    JdbcDatabaseDriverFactory(urlBuilder),
)
