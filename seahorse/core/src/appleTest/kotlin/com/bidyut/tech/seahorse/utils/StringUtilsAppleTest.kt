package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsAppleTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check Apple is mentioned",
            formatString("Check Apple is mentioned")
        )
        assertEquals(
            "Check Apple is mentioned",
            formatString(sanitiseFormatString("Check %s is mentioned"), "Apple")
        )
        assertEquals(
            "Check Apple is mentioned",
            formatString(sanitiseFormatString("Check %s is %s"), "Apple", "mentioned")
        )
        assertEquals(
            "Check Apple is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Apple", "mentioned")
        )
        assertEquals(
            "Check Apple is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Apple", "mentioned")
        )
        assertEquals(
            "Check Apple is mentioned",
            formatString(sanitiseFormatString("%s %s %s %s"), "Check", "Apple", "is", "mentioned")
        )
        assertEquals(
            "Check Apple is mentioned here",
            formatString(sanitiseFormatString("%s %s %s %s %s"), "Check", "Apple", "is", "mentioned", "here")
        )
    }
}
