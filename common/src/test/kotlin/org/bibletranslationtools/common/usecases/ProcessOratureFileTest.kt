package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.FileData
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class ProcessOratureFileTest {
    private val oratureFileName = "orature_file.zip"
    private val expectedWavFiles = 2

    @Test
    fun testExtractAudioFiles() {
        val files = ProcessOratureFile(getOratureFile()).extractAudio()

        assertEquals(expectedWavFiles, files.size)

        files.forEach { file ->
            val result = ParseFileName(file).parse().test()

            // imported file names should be valid for parser
            result.assertValue {
                it.language != null
                it.resourceType != null
                it.book != null
                it.chapter != null
            }
        }
    }

    private fun getOratureFile(): File {
        val resource = javaClass.classLoader.getResource(oratureFileName)
                ?: throw FileNotFoundException("Test resource not found: $oratureFileName")
        return File(resource.path)
    }
}