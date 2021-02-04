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
class ProcessOratureFile(file: File) {
    private val zipFile: ZipFile

    init {
        if (!isOratureFormat(file)) throw IOException("Invalid Orature file.")
        zipFile = ZipFile(file)
    }

    fun extractAudio(): List<File> {
        val tempDir = createTempDir().apply { deleteOnExit() }

        zipFile.entries().iterator().forEach { entry ->
            if (
                    entry.name.startsWith("content") &&
                    File(entry.name).extension == MediaExtension.WAV.toString()
            ) {
                extractEntry(entry, tempDir)
            }
        }
        zipFile.close()

        return tempDir.listFiles()?.toList() ?: listOf()
    }

    private fun extractEntry(entry: ZipEntry, directory: File) {
        val fileEntry = File(entry.name)
        val destFile = directory.resolve(fileEntry.name).apply { deleteOnExit() }

        zipFile.getInputStream(entry).buffered().use { input ->
            destFile.outputStream().buffered().use { output ->
                output.write(input.readBytes())
            }
        }
    }

    companion object {
        private const val manifestName = "manifest.yaml"
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