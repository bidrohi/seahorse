package com.bidyut.tech.seahorse.data.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class NativeDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String,
    ): SqlDriver {
        return NativeSqliteDriver(
            schema,
            "$databaseName.db"
        )
    }
}
