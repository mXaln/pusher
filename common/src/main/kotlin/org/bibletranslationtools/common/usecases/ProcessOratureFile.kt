package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File

/**
 *  @throws Exception if constructor @parameter file
 *  is not a valid Orature file
 */
class ProcessOratureFile(private val file: File) {
    private val creatorName = "Orature"
    private val mediaExtension = MediaExtension.WAV.toString()

    init {
        validate()
    }

    @Throws(Exception::class)
    private fun validate() {
        try {
            ResourceContainer.load(file).use {
                if (it.manifest.dublinCore.creator != creatorName) {
                    throw Exception()
                }
            }
        } catch (ex: Exception) {
            throw Exception("Invalid Orature file")
        }
    }

    @Throws(Exception::class)
    fun extractAudio(): List<File> {
        val tempDir = createTempDir().apply { deleteOnExit() }

        ResourceContainer.load(file).use { rc ->
            val content = rc.getProjectContent(extension=mediaExtension)
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

    companion object {
        fun isOratureFormat(file: File): Boolean = file.extension == "zip"
    }
}
