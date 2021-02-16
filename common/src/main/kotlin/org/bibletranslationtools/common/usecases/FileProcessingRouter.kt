package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.validators.OratureValidator
import java.io.File
import java.io.IOException

class FileProcessingRouter(private val file: File) {

    @Throws(IOException::class)
    fun execute(): List<File> {
        return when {
            file.extension == "zip" && OratureValidator(file).isValid() ->
                ProcessOratureFile(file).process()

            file.extension == "zip" -> throw IOException("Invalid zip file") // this error will be emitted to UI

            else -> listOf(file)
        }
    }
}