package org.bibletranslationtools.maui.common.data

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class MediaQualityTest {

    @Test
    fun createValidMediaQuality() {
        Assert.assertEquals(MediaQuality.of("hi"), MediaQuality.HI)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidMediaQualityThrowsException() {
        MediaQuality.of("wrong")
    }
}
