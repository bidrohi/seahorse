package com.bidyut.tech.seahorse.data

import android.content.res.Resources
import com.bidyut.tech.seahorse.model.LanguageId
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.versioning.AndroidVersions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Config(sdk = [AndroidVersions.N.SDK_INT])
@RunWith(RobolectricTestRunner::class)
class ResourceFallbackSourceTest {
    @Test
    fun `ensure that fallback string works for all languages`() {
        val languageSlot = slot<LanguageId>()

        val resources = mockk<Resources>(relaxed = true)
        every { resources.getIdentifier("key", "string", any()) } answers {
            if (!languageSlot.isCaptured) {
                1
            } else if (languageSlot.captured == "bn") {
                2
            } else {
                0
            }
        }
        every { resources.getIdentifier("missingKey", "string", any()) } returns 0
        every { resources.getString(1, *anyVararg()) } returns "value"
        every { resources.getString(2, *anyVararg()) } returns "মান"

        val context = spyk(RuntimeEnvironment.getApplication())
        every { context.createConfigurationContext(any()) } returns context
        every { context.resources } returns resources

        val source = spyk(ResourceFallbackSource(context))
        every { source.setLanguageId(capture(languageSlot)) } answers { callOriginal() }

        assertNull(source.getStringByKey("missingKey"))
        assertEquals("value", source.getStringByKey("key"))
        source.setLanguageId("bn")
        assertEquals("মান", source.getStringByKey("key"))
        source.setLanguageId("es")
        assertNull(source.getStringByKey("key"))
    }
}
