package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class ProcessOratureFileTest {
    private val oratureFileName = "orature_file.zip"
    private val expectedWavFiles = 2

    @Test
    fun testExtractAudioFiles() {
        val extension = MediaExtension.WAV.toString()
        val files = ProcessOratureFile(getOratureFile()).extractAudio(extension)

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

    @Test(expected = Exception::class)
    fun testNonOratureZipFile() {
        val fakeZip = createTempFile(suffix = ".zip").apply { deleteOnExit() }

        // this invalid file will throw an exception
        ProcessOratureFile(fakeZip)
    }

    private fun getOratureFile(): File {
        val resource = javaClass.classLoader.getResource(oratureFileName)
                ?: throw FileNotFoundException("Test resource not found: $oratureFileName")
        return File(resource.path)
    }
}