package org.bibletranslationtools.maui.common.data

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class MediaExtensionTest {

    @Test
    fun createValidMediaExtension() {
        Assert.assertEquals(MediaExtension.of("wav"), MediaExtension.WAV)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidMediaExtensionThrowsException() {
        MediaExtension.of("wrong")
    }
}
