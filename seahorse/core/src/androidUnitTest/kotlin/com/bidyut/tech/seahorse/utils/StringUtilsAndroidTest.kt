package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsAndroidTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check Android is mentioned",
            formatString("Check Android is mentioned")
        )
        assertEquals(
            "Check Android is mentioned",
            formatString("Check %s is mentioned", "Android")
        )
        assertEquals(
            "Check Android is mentioned",
            formatString("Check %s is %s", "Android", "mentioned")
        )
    }

    @Test
    fun testPercentageFormat() {
        assertEquals(
            "100%",
            formatString("100%")
        )
        assertEquals(
            "1% of 100%",
            formatString("%s%% of 100%%", "1")
        )
        assertEquals(
            "%s%% of 100%%",
            formatString("%s%% of 100%%")
        )
    }
}
