package org.bibletranslationtools.common.usecases

import io.reactivex.Single
import org.bibletranslationtools.common.data.FileData
import java.io.File

class MakePath(private val fileData: FileData) {

    companion object {
        private const val CONTENTS = "CONTENTS"
    }

    private val filename = normalizeFileName(fileData.file)

    fun build(): Single<String> {
        return Single.fromCallable {
            validateFileData()

            val initialPath = buildInitialPath()

            when {
                fileData.isContainerAndCompressed -> {
                    arrayOf(
                        initialPath,
                        fileData.mediaExtension,
                        fileData.mediaQuality,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
                fileData.isContainer -> {
                    arrayOf(
                        initialPath,
                        fileData.mediaExtension,
                        fileData.grouping,
                        filename
                    ).joinToString("/")
                }
                fileData.isCompressed -> {
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
            (fileData.isContainer || fileData.isContainerAndCompressed) && fileData.mediaExtension == null -> {
                throw IllegalArgumentException("Media extension needs to be specified for container")
            }
            (fileData.isCompressed || fileData.isContainerAndCompressed) && fileData.mediaQuality == null -> {
                throw IllegalArgumentException("Media quality needs to be specified for compressed media")
            }
            !fileData.isContainer && fileData.mediaExtension != null -> {
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
                    fileData.extension
                ).joinToString("/")
            }
            fileData.chapter != null -> {
                arrayOf(
                    fileData.language,
                    fileData.resourceType,
                    fileData.book,
                    fileData.chapter,
                    CONTENTS,
                    fileData.extension
                ).joinToString("/")
            }
            else -> {
                arrayOf(
                    fileData.language,
                    fileData.resourceType,
                    fileData.book,
                    CONTENTS,
                    fileData.extension
                ).joinToString("/")
            }
        }
    }

    private fun normalizeFileName(file: File): String {
        val filenameWithoutExtension = file.nameWithoutExtension.toLowerCase()
        return "$filenameWithoutExtension.${fileData.extension}"
    }
}
