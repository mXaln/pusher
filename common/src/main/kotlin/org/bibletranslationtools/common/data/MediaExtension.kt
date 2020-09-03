package org.bibletranslationtools.common.data

enum class MediaExtension(vararg val ext: String) {
    WAV("wav"),
    MP3("mp3"),
    JPG("jpg", "jpeg");

    companion object {
        fun of(ext: String) =
            values().singleOrNull {
                it.name == ext.toUpperCase() || it.ext.contains(ext)
            } ?: throw IllegalArgumentException("Media extension $ext is not supported")
    }

    override fun toString(): String {
        return ext.first()
    }
}
