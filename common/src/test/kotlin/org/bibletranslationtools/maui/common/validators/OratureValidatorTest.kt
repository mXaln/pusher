package org.bibletranslationtools.maui.common.validators

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class OratureValidatorTest {
    private val oratureFileName = "orature_file.zip"

    @Test
    fun testGoodOratureZipFile() {
        val file = getOratureFile()

        assertTrue(OratureValidator(file).isValid())
    }

    @Test
    fun testBadOratureZipFile() {
        val fakeZip = createTempFile(suffix = ".zip").apply { deleteOnExit() }

        assertFalse(OratureValidator(fakeZip).isValid())
    }

    private fun getOratureFile(): File {
        val resource = javaClass.classLoader.getResource(oratureFileName)
                ?: throw FileNotFoundException("Test resource not found: $oratureFileName")
        return File(resource.path)
    }
}