package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.fileprocessor.*
import java.io.File
import java.io.IOException
import java.util.Queue
import java.util.LinkedList

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
            val status = it.process(file, fileQueue, resultList)
            if (status == FileStatus.PROCESSED) return
        }
        // file was not processed by any processor
        val rejected = FileResult(
                status = FileStatus.REJECTED,
                data = null,
                requestedFile = file
        )
        resultList.add(rejected)
    }

    companion object {
        fun build(): FileProcessingRouter {
            val processorList: List<FileProcessor> = listOf(
                    CueProcessor(),
                    JpgProcessor(),
                    Mp3Processor(),
                    TrProcessor(),
                    WavProcessor(),
                    OratureFileProcessor()
            )
            return FileProcessingRouter(processorList)
        }
    }
}