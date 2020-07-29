package org.bibletranslationtools.common.usecases

import io.reactivex.Single
import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.common.extensions.CompressedExtensions
import org.bibletranslationtools.common.extensions.ContainerExtensions
import org.bibletranslationtools.common.extensions.NormalizableExtensions
import java.io.File
import java.lang.IllegalArgumentException

class MakePath(private val fileData: FileData) {

    companion object {
        private const val CONTENTS = "CONTENTS"
    }

    private val extension = normalizeFileExtension(fileData.file.extension)
    private val filename = normalizeFileName(fileData.file)

    private val isContainer = ContainerExtensions.isValid(extension)
    private val isContainerAndCompressed =
        isContainer && CompressedExtensions.isValid(fileData.mediaExtension.toString())
    private val isCompressed =
        !isContainer && CompressedExtensions.isValid(extension)

    fun build(): Single<String> {
        return Single.fromCallable {
            validateFileData()

            val initialPath = buildInitialPath()

            when {
                isContainerAndCompressed -> {
                    arrayOf(
                        initialPath,
                        fileData.mediaExtension,
                        fileData.mediaQuality,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
                isContainer -> {
                    arrayOf(
                        initialPath,
                        fileData.mediaExtension,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
                isCompressed -> {
                    arrayOf(
                        initialPath,
                        fileData.mediaQuality,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
                else -> {
                    arrayOf(
                        initialPath,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
            }
        }
    }

    private fun validateFileData() {
        when {
            fileData.language.isNullOrBlank() -> {
                throw IllegalArgumentException("Language should be specified")
            }
            fileData.resourceType == null -> {
                throw IllegalArgumentException("Resource type should be specified")
            }
            fileData.chapter != null && fileData.book == null -> {
                throw IllegalArgumentException("Book needs to be specified")
            }
            fileData.grouping == null -> {
                throw IllegalArgumentException("Grouping needs to be specified")
            }
            (isContainer || isContainerAndCompressed) && fileData.mediaExtension == null -> {
                throw IllegalArgumentException("Media extension needs to be specified for container")
            }
            (isCompressed || isContainerAndCompressed) && fileData.mediaQuality == null -> {
                throw IllegalArgumentException("Media quality needs to be specified for compressed media")
            }
            !isContainer && fileData.mediaExtension != null -> {
                throw IllegalArgumentException("Media extension cannot be applied to non-container media")
            }
        }
    }

    private fun buildInitialPath(): String {
        return when {
            fileData.book.isNullOrBlank() -> {
                arrayOf(
                    fileData.language,
                    fileData.resourceType,
                    CONTENTS,
                    extension
                ).joinToString("/")
            }
            fileData.chapter != null -> {
                arrayOf(
                    fileData.language,
                    fileData.resourceType,
                    fileData.book,
                    fileData.chapter,
                    CONTENTS,
                    extension
                ).joinToString("/")
            }
            else -> {
                arrayOf(
                    fileData.language,
                    fileData.resourceType,
                    fileData.book,
                    CONTENTS,
                    extension
                ).joinToString("/")
            }
        }
    }

    private fun normalizeFileExtension(extension: String): String {
        val ext = extension.toLowerCase()
        return if (NormalizableExtensions.isValid(ext)) {
            NormalizableExtensions.of(extension).toString()
        } else {
            ext
        }
    }

    private fun normalizeFileName(file: File): String {
        val filenameWithoutExtension = file.nameWithoutExtension.toLowerCase()
        return if (NormalizableExtensions.isValid(file.extension)) {
            "$filenameWithoutExtension.${NormalizableExtensions.of(file.extension)}"
        } else {
            file.name.toLowerCase()
        }
    }
}
