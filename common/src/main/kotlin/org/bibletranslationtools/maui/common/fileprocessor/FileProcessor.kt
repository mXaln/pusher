package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileData
import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.usecases.ParseFileName
import java.io.File
import java.util.Queue

abstract class FileProcessor {
    abstract fun process(
        file: File,
        fileQueue: Queue<File>,
        resultList: MutableList<FileResult>
    ): FileStatus

    protected fun getFileData(file: File): FileData {
        return ParseFileName(file).read()
    }
}