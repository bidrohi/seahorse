package com.bidyut.tech.seahorse.data.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class JdbcDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String,
    ): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databaseName.db")
        schema.create(driver)
        return driver
    }
}
