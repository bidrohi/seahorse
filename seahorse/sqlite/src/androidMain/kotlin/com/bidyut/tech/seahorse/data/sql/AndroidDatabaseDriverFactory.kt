package com.bidyut.tech.seahorse.data.sql

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi

@SeahorseInternalApi
class AndroidDatabaseDriverFactory(
    private val context: Context,
) : DatabaseDriverFactory {
    override fun createDriver(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String
    ): SqlDriver {
        return AndroidSqliteDriver(
            schema,
            context,
            "$databaseName.db"
        )
    }
}
