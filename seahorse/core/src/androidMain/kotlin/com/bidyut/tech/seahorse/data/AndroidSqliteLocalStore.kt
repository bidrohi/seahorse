package com.bidyut.tech.seahorse.data

import android.content.Context
import com.bidyut.tech.seahorse.data.sql.AndroidDatabaseDriverFactory

class AndroidSqliteLocalStore(
    context: Context,
) : SqliteLocalStore(
    AndroidDatabaseDriverFactory(context),
)
