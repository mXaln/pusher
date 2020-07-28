package org.bibletranslationtools.common.data

import org.bibletranslationtools.common.extensions.CompressedExtensions
import org.bibletranslationtools.common.extensions.ContainerExtensions
import java.io.File

data class FileData(
    val file: File,
    var language: String? = null,
    var resourceType: ResourceType? = null,
    var book: String? = null,
    var chapter: Int? = null,
    var mediaExtension: MediaExtension? = null,
    var mediaQuality: MediaQuality? = null,
    var grouping: Grouping? = null
) {
    var mediaExtensionAvailable: Boolean = true
        get() = mediaExtensionAvailable()

    var mediaQualityAvailable: Boolean = true
        get() = mediaQualityAvailable()

    private fun mediaExtensionAvailable(): Boolean {
        return ContainerExtensions.isValid(file.extension)
    }

    private fun mediaQualityAvailable(): Boolean {
        return mediaExtensionAvailable || CompressedExtensions.isValid(file.extension)
    }
}
