package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.fileprocessor.FileProcessor
import java.io.File
import java.io.IOException
import java.util.*

class FileProcessingRouter(private val processors: List<FileProcessor>) {
    private val fileQueue: Queue<File> = LinkedList<File>()

    @Throws(IOException::class)
    fun handleFiles(files: List<File>): List<FileResult> {
        val resultList = mutableListOf<FileResult>()
        fileQueue.addAll(files)

        while (fileQueue.isNotEmpty()) {
            processFile(fileQueue.poll(), resultList)
        }

        return resultList
    }

    private fun processFile(file: File, resultList: MutableList<FileResult>) {
        processors.forEach {
            it.process(file, fileQueue, resultList)
        }
    }
}