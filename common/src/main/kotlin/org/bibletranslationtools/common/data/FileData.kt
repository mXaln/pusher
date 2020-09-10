package org.bibletranslationtools.common.data

import org.bibletranslationtools.common.extensions.CompressedExtensions
import org.bibletranslationtools.common.extensions.ContainerExtensions
import org.bibletranslationtools.common.extensions.MediaExtensions
import java.io.File

data class FileData(
    val file: File,
    val language: String? = null,
    val resourceType: ResourceType? = null,
    val book: String? = null,
    val chapter: Int? = null,
    val mediaExtension: MediaExtension? = null,
    val mediaQuality: MediaQuality? = null,
    val grouping: Grouping? = null
) {
    val extension = MediaExtensions.of(file.extension)

    val isContainer = ContainerExtensions.isSupported(extension.norm)

    val isCompressed =
        !isContainer && CompressedExtensions.isSupported(extension.norm)

    val isContainerAndCompressed =
        isContainer && CompressedExtensions.isSupported(mediaExtension.toString())
}
