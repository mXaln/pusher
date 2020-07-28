package org.bibletranslationtools.common.extensions

import org.junit.Assert
import org.junit.Test
import java.io.File

class ContainerExtensionsTest {

    private val trFile = File("test.tr")
    private val wavFile = File("test.wav")

    @Test
    fun testValidContainerExtension() {
        Assert.assertTrue(ContainerExtensions.isValid(trFile.extension))
    }

    @Test
    fun testNotValidContainerExtension() {
        Assert.assertFalse(ContainerExtensions.isValid(wavFile.extension))
    }
}
