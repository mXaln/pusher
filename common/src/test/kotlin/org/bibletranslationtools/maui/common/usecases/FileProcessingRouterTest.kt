package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.fileprocessor.FileProcessor
import org.bibletranslationtools.maui.common.fileprocessor.OratureFileProcessor
import org.bibletranslationtools.maui.common.fileprocessor.WavProcessor
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class FileProcessingRouterTest {
    private val oratureFile = "orature_file.zip" // orature file contains 2 bad wav files
    private val wavFile = "en_ulb_b41_mat_c01.wav"
    private val badFile =  "fake.jpg"
    private val expectedResultSize = 4

    @Test
    fun testHandleFiles() {
        val files = listOf(
                getTestFile(oratureFile),
                getTestFile(wavFile),
                getTestFile(badFile)
        )
        val processors: List<FileProcessor> = listOf(
                OratureFileProcessor(),
                WavProcessor()
        )

        val result = FileProcessingRouter(processors).handleFiles(files)
        val rejectedFileCount = result.filter {
            it.status == FileStatus.REJECTED
        }.size
        val processedFileCount = result.filter {
            it.status == FileStatus.PROCESSED
        }.size

        assertEquals(expectedResultSize, result.size)
        assertEquals(1, processedFileCount)
        assertEquals(3, rejectedFileCount)
    }

    private fun getTestFile(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
                ?: throw FileNotFoundException("Test resource not found: $fileName")
        return File(resource.path)
    }
}