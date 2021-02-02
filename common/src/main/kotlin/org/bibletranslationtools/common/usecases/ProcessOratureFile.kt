package org.bibletranslationtools.common.usecases

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.bibletranslationtools.common.data.MediaExtension
import java.io.File
import java.io.IOException
import java.util.zip.ZipFile

class ProcessOratureFile(private val file: File) {
    private val manifestName = "manifest.yaml"

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

    @Throws(IOException::class)
    fun extractAudio(): List<File> {
        // check if  orature file
        if (!isOrature()) throw IOException("Invalid Orature file.")

        val tempDir = createTempDir().apply { deleteOnExit() }
        val zipFile = ZipFile(file)
        zipFile.entries().iterator().forEach { entry ->
            val fileEntry = File(entry.name)

            if (entry.name.startsWith("content") &&
                    fileEntry.extension == MediaExtension.WAV.toString()
            ) {
                val destFile = tempDir.resolve(fileEntry.name)
                zipFile.getInputStream(entry).buffered().use { input ->
                    destFile.outputStream().buffered().use { output ->
                        output.write(input.readBytes())
                    }
                }
                destFile.deleteOnExit()
            }
        }
        println(tempDir)
        return tempDir.listFiles()?.toList() ?: listOf()
    }

    fun isOrature(): Boolean {
        val zipFile = ZipFile(file)
        val manifestEntry = zipFile.getEntry(manifestName) ?: return false

        return try {
            val mapper = ObjectMapper(YAMLFactory())
            val manifest: Manifest = mapper.readValue(
                    zipFile.getInputStream(manifestEntry)
            )
            manifest.dublinCore.creator == "Orature"
        } catch (ex: Exception) {
            false
        }
    }
}