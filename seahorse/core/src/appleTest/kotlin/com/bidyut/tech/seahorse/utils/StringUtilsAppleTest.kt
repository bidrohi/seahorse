package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsAppleTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check iOS is mentioned",
            formatString("Check iOS is mentioned")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString(sanitiseFormatString("Check %s is mentioned"), "iOS")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString(sanitiseFormatString("Check %s is %s"), "iOS", "mentioned")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "iOS", "mentioned")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "iOS", "mentioned")
        )
        assertEquals(
            "Check iOS is mentioned",
            formatString(sanitiseFormatString("%s %s %s %s"), "Check", "iOS", "is", "mentioned")
        )
        assertEquals(
            "Check iOS is mentioned here",
            formatString(sanitiseFormatString("%s %s %s %s %s"), "Check", "iOS", "is", "mentioned", "here")
        )
    }
}
