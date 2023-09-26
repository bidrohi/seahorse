package com.bidyut.tech.seahorse.data

import android.content.Context
import com.bidyut.tech.seahorse.data.sql.DatabaseDriverFactory

class AndroidSqliteLocalStore(
    context: Context,
) : SqliteLocalStore(
    DatabaseDriverFactory(context),
)
