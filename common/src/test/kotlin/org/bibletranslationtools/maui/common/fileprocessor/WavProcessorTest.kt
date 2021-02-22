package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.util.Queue
import java.util.LinkedList

class WavProcessorTest {
    private val goodWavFile = "en_ulb_b41_mat_c01.wav"

    @Test
    fun testProcessGoodFile() {
        val file = getTestFile(goodWavFile)
        val queue: Queue<File> = LinkedList<File>()
        val resultList = mutableListOf<FileResult>()
        val status = WavProcessor().process(file, queue, resultList)

        assertEquals(FileStatus.PROCESSED, status)
        assertEquals(1, resultList.size)
        assertEquals(0, queue.size)
    }

    private fun getTestFile(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
                ?: throw FileNotFoundException("Test resource not found: $fileName")
        return File(resource.path)
    }
}