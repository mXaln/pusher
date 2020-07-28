package org.bibletranslationtools.common.extensions

import org.junit.Assert
import org.junit.Test
import java.io.File

class CompressedExtensionsTest {

    private val mp3File = File("test.mp3")
    private val wavFile = File("test.wav")

    @Test
    fun testValidCompressedExtension() {
        Assert.assertTrue(CompressedExtensions.isValid(mp3File.extension))
    }

    @Test
    fun testNotValidCompressedExtension() {
        Assert.assertFalse(CompressedExtensions.isValid(wavFile.extension))
    }
}
