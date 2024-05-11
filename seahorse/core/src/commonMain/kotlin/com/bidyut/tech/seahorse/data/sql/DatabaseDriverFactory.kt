package com.bidyut.tech.seahorse.data.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

interface DatabaseDriverFactory {
    fun createDriver(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String,
    ): SqlDriver
}
