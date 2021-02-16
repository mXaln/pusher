package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.lang.Exception

class FileProcessingRouter(private val file: File) {

    @Throws(IOException::class)
    fun execute(): List<File> {
        return when {
            file.extension == "zip" && ProcessOratureFile.isOratureFormat(file) ->
                ProcessOratureFile(file).process()

            file.extension == "zip" -> throw IOException("Invalid zip file") // this error will be emitted to UI

            else -> listOf(file)
        }
    }
}