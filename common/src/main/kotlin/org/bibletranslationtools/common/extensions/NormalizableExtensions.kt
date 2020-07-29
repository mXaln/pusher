package org.bibletranslationtools.common.extensions

import org.bibletranslationtools.common.data.MediaExtension
import java.lang.IllegalArgumentException

enum class NormalizableExtensions(vararg val ext: String) {
    JPG("jpg", "jpeg");

    companion object {
        fun of(ext: String) =
            MediaExtension.values().singleOrNull {
                it.name == ext.toUpperCase() || it.ext.any { _ext -> _ext == ext }
            } ?: throw IllegalArgumentException("Extension $ext is not normalizable")

        fun isValid(ext: String) =
            CompressedExtensions.values().any {
                it.name == ext.toUpperCase() || it.ext.any { _ext -> _ext == ext }
            }
    }

    override fun toString(): String {
        return ext.first()
    }
}
