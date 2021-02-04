package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 *  @throws IOException if the file passed into constructor is not from Orature
 */
class ProcessOratureFile(private val file: File) {

    init {
        if (!isOratureFormat(file)) throw IOException("Invalid Orature file.")
    }

    /**
     * This method extracts audio files under a specific directory within the zip
     * Regard Orature resource container for more detail
     */
    fun extractAudio(): List<File> {
        val tempDir = createTempDir().apply { deleteOnExit() }
        val zipFile = ZipFile(file)

        zipFile.entries().iterator().forEach { entry ->
            if (
                    entry.name.startsWith("content") &&
                    File(entry.name).extension == MediaExtension.WAV.toString()
            ) {
                extractEntry(entry, zipFile, tempDir)
            }
        }
        zipFile.close()

        return tempDir.listFiles()?.toList() ?: listOf()
    }

    private fun extractEntry(entry: ZipEntry, zipFile: ZipFile, directory: File) {
        val fileEntry = File(entry.name)
        val destFile = directory.resolve(fileEntry.name).apply { deleteOnExit() }

        zipFile.getInputStream(entry).buffered().use { input ->
            destFile.outputStream().buffered().use { output ->
                output.write(input.readBytes())
            }
        }
    }

    companion object {
        private const val creatorName = "Orature"

        fun isOratureFormat(file: File): Boolean {
            if (file.extension != "zip") return false

            try {
                ResourceContainer.load(file).use {
                    return it.manifest.dublinCore.creator == creatorName
                }
            } catch (ex: Exception) {
                return false
            }
        }
    }
}