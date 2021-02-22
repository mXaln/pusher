package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.data.MediaExtension
import org.bibletranslationtools.maui.common.fileprocessor.OratureFileProcessor
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class OratureFileProcessorTest {
    private val oratureFileName = "orature_file.zip"
    private val expectedWavFiles = 2

    @Test
    fun testExtractAudioFiles() {
        val extension = MediaExtension.WAV.toString()
        val oratureFile = getOratureFile()
        val files = OratureFileProcessor().extractAudio(oratureFile, extension)

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

    @Test
    fun testOratureProcessor() {
        val oratureFile = getOratureFile()
        val exportList = mutableListOf<FileResult>()
        val queue: Queue<File> = LinkedList<File>()
        val status = OratureFileProcessor().process(oratureFile, queue, exportList)

        assertEquals(FileStatus.PROCESSED, status)
        assertEquals(2, queue.size)
        assertEquals(0, exportList.size)
    }

    private fun getOratureFile(): File {
        val resource = javaClass.classLoader.getResource(oratureFileName)
                ?: throw FileNotFoundException("Test resource not found: $oratureFileName")
        return File(resource.path)
    }
}