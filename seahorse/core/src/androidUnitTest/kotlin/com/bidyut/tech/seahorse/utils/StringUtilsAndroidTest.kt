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
}
