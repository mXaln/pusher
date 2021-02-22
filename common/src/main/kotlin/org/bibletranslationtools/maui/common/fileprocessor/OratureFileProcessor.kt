package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.data.MediaExtension
import org.bibletranslationtools.maui.common.validators.OratureValidator
import org.slf4j.LoggerFactory
import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File
import java.io.IOException
import java.util.Queue

class OratureFileProcessor: FileProcessor() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun process(
            file: File,
            fileQueue: Queue<File>,
            resultList: MutableList<FileResult>
    ): FileStatus {
        if (!OratureValidator(file).isValid()) {
            return FileStatus.REJECTED
        }

        return try {
            val extension = MediaExtension.WAV.toString()
            val extractedFiles = extractAudio(file, extension)
            fileQueue.addAll(extractedFiles)

            FileStatus.PROCESSED
        } catch (ex: IOException) {
            logger.error(
                    "An error occurred while extracting audio from Orature file: $file", ex
            )
            FileStatus.REJECTED
        }
    }

    @Throws(IOException::class)
    fun extractAudio(file: File, extension: String): List<File> {
        val tempDir = createTempDir().apply { deleteOnExit() }

        ResourceContainer.load(file).use { rc ->
            val content = rc.getProjectContent(extension=extension)
                    ?: return listOf()

            content.streams.forEach { entry ->
                // resolve chapter file name for parser compatibility
                val normalizedFileName = File(entry.key).name.replace("_meta", "")
                val destFile = tempDir.resolve(normalizedFileName)
                destFile.deleteOnExit()

                entry.value.buffered().use { input ->
                    destFile.outputStream().buffered().use { output ->
                        output.write(input.readBytes())
                    }
                }
            }
        }

        return tempDir.listFiles()?.toList() ?: listOf()
    }
}
