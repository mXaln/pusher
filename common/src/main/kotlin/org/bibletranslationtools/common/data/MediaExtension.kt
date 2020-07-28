package org.bibletranslationtools.common.data

import java.lang.IllegalArgumentException

enum class MediaExtension(vararg val ext: String) {
    WAV("wav"),
    MP3("mp3"),
    JPG("jpg", "jpeg");

    companion object {
        fun of(ext: String) =
            values().singleOrNull {
                it.name == ext.toUpperCase() || it.ext.any { _ext -> _ext == ext }
            } ?: throw IllegalArgumentException("There is no media extension $ext")
    }
}
