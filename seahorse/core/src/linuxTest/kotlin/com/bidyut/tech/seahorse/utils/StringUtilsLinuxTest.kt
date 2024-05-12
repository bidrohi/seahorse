package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsLinuxTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check Linux is mentioned",
            formatString("Check Linux is mentioned")
        )
        assertEquals(
            "Check Linux is mentioned",
            formatString(sanitiseFormatString("Check %s is mentioned"), "Linux")
        )
        assertEquals(
            "Check Linux is mentioned",
            formatString(sanitiseFormatString("Check %s is %s"), "Linux", "mentioned")
        )
        assertEquals(
            "Check Linux is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Linux", "mentioned")
        )
        assertEquals(
            "Check Linux is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Linux", "mentioned")
        )
        assertEquals(
            "Check Linux is mentioned",
            formatString(sanitiseFormatString("%s %s %s %s"), "Check", "Linux", "is", "mentioned")
        )
        assertEquals(
            "Check Linux is mentioned here",
            formatString(sanitiseFormatString("%s %s %s %s %s"), "Check", "Linux", "is", "mentioned", "here")
        )
    }
}
