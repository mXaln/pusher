package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import java.io.File
import java.util.Queue

interface FileProcessor {
    fun process(
            file: File,
            fileQueue: Queue<File>,
            resultList: MutableList<FileResult>
    )
}