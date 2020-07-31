package org.bibletranslationtools.common.data

import org.bibletranslationtools.common.extensions.CompressedExtensions
import org.bibletranslationtools.common.extensions.ContainerExtensions
import org.bibletranslationtools.common.extensions.MediaExtensions
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
    val extension = MediaExtensions.of(file.extension)

    val isContainer = ContainerExtensions.isSupported(extension.norm)

    val isCompressed =
        !isContainer && CompressedExtensions.isSupported(extension.norm)

    val isContainerAndCompressed =
        isContainer && CompressedExtensions.isSupported(mediaExtension.toString())

    var mediaExtensionAvailable = isContainer

    var mediaQualityAvailable = isContainerAndCompressed || isCompressed
}
