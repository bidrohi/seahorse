package com.bidyut.tech.seahorse.data.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class JdbcDatabaseDriverFactory(
    private val urlBuilder: (String) -> String,
) : DatabaseDriverFactory {
    override fun createDriver(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String,
    ): SqlDriver = JdbcSqliteDriver(urlBuilder(databaseName)).apply {
        schema.create(this)
    }
}
