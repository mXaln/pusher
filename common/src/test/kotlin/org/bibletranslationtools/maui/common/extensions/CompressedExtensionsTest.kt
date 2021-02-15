package org.bibletranslationtools.maui.common.extensions

import org.junit.Assert
import org.junit.Test
import java.io.File

class CompressedExtensionsTest {

    private val mp3File = File("test.mp3")
    private val wavFile = File("test.wav")

    @Test
    fun testValidCompressedExtension() {
        Assert.assertTrue(CompressedExtensions.isSupported(mp3File.extension))
    }

    @Test
    fun testNotValidCompressedExtension() {
        Assert.assertFalse(CompressedExtensions.isSupported(wavFile.extension))
    }
}
