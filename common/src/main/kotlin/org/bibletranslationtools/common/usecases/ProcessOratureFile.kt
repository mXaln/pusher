package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import org.slf4j.LoggerFactory
import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File
import java.io.IOException

class ProcessOratureFile(private val file: File) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun process(): List<File> {
        return try {
            val extension = MediaExtension.WAV.toString()
            extractAudio(extension)
        } catch (ex: IOException) {
            logger.error(
                    "An error occurred while extracting audio from Orature file: $file", ex
            )
            listOf()
        }
    }

    @Throws(IOException::class)
    fun extractAudio(extension: String): List<File> {
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
