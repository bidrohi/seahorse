package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsIosTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check iOS is mentioned",
            formatString("Check iOS is mentioned")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString("Check %s is mentioned", "iOS")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString("Check %s is %s", "iOS", "mentioned")
        )
    }
}
