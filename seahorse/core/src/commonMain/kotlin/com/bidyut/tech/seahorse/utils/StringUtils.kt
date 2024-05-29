package com.bidyut.tech.seahorse.utils

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi


@SeahorseInternalApi
expect fun sanitiseFormatString(string: String): String

@SeahorseInternalApi
expect fun formatString(
    fmt: String,
    vararg args: Any,
): String
