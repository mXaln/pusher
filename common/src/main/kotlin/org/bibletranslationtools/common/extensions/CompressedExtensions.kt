package org.bibletranslationtools.common.extensions

enum class CompressedExtensions(vararg val ext: String) {
    MP3("mp3"),
    JPG("jpg", "jpeg");

    companion object {
        fun isValid(ext: String) =
            values().any {
                it.name == ext.toUpperCase() || it.ext.any { _ext -> _ext == ext }
            }
    }
}
