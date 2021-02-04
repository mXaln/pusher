package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.MediaExtension
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ProcessOratureFile(private val file: File) {

    /**
     * This method extracts audio files under a specific directory within the zip
     * Regard Orature resource container for more detail
     */
    fun extractAudio(): List<File> {
        val tempDir = createTempDir().apply { deleteOnExit() }
        val zipFile = ZipFile(file)

        zipFile.entries().iterator().forEach { entry ->
            val isWav = File(entry.name).extension == MediaExtension.WAV.toString()
            if (entry.name.startsWith("content") && isWav) {
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
        fun isOratureFormat(file: File): Boolean = file.extension == "zip"
    }
}
