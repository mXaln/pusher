package org.bibletranslationtools.common.validators

import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class OratureValidatorTest {
    private val oratureFileName = "orature_file.zip"

    @Test
    fun testGoodOratureZipFile() {
        val file = getOratureFile()
        OratureValidator(file).validate()
    }

    @Test(expected = Exception::class)
    fun testBadOratureZipFile() {
        val fakeZip = createTempFile(suffix = ".zip").apply { deleteOnExit() }

        // this invalid file will throw an exception
        OratureValidator(fakeZip).validate()
    }

    private fun getOratureFile(): File {
        val resource = javaClass.classLoader.getResource(oratureFileName)
                ?: throw FileNotFoundException("Test resource not found: $oratureFileName")
        return File(resource.path)
    }
}