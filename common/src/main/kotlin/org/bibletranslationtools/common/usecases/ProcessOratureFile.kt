package org.bibletranslationtools.common.usecases

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.bibletranslationtools.common.data.MediaExtension
import java.io.File
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 *  @throws IOException if the file passed into constructor is not from Orature
 */
class ProcessOratureFile(file: File) {
    private val zipFile: ZipFile

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Manifest(
            @JsonProperty("dublin_core")
            var dublinCore: DublinCore
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class DublinCore(
            @JsonProperty("creator")
            var creator: String
    )

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
            val zipFile = ZipFile(file)
            val manifestEntry = zipFile.getEntry(manifestName) ?: return false

            return try {
                val mapper = ObjectMapper(YAMLFactory())
                val manifest: Manifest = mapper.readValue(
                        zipFile.getInputStream(manifestEntry)
                )
                manifest.dublinCore.creator == creatorName
            } catch (ex: Exception) {
                false
            }
        }
    }
}