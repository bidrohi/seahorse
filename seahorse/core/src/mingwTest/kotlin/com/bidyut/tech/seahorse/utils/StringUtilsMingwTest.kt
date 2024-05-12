package com.bidyut.tech.seahorse.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsMingwTest {
    @Test
    fun testFormat() {
        assertEquals(
            "Check Mingw is mentioned",
            formatString("Check Mingw is mentioned")
        )
        assertEquals(
            "Check Mingw is mentioned",
            formatString(sanitiseFormatString("Check %s is mentioned"), "Mingw")
        )
        assertEquals(
            "Check Mingw is mentioned",
            formatString(sanitiseFormatString("Check %s is %s"), "Mingw", "mentioned")
        )
        assertEquals(
            "Check Mingw is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Mingw", "mentioned")
        )
        assertEquals(
            "Check Mingw is mentioned",
            formatString(sanitiseFormatString("%s %s is %s"), "Check", "Mingw", "mentioned")
        )
        assertEquals(
            "Check Mingw is mentioned",
            formatString(sanitiseFormatString("%s %s %s %s"), "Check", "Mingw", "is", "mentioned")
        )
        assertEquals(
            "Check Mingw is mentioned here",
            formatString(sanitiseFormatString("%s %s %s %s %s"), "Check", "Mingw", "is", "mentioned", "here")
        )
    }
}
