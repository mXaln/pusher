package org.bibletranslationtools.maui.common.extensions

enum class MediaExtensions(vararg val ext: String) {
    TR("tr"),
    MP3("mp3"),
    WAV("wav"),
    JPG("jpg", "jpeg"),
    CUE("cue");

    val norm = ext.first()

    companion object: SupportedExtensions {
        override fun isSupported(ext: String) =
            values().any {
                it.name == ext.toUpperCase() || it.ext.contains(ext)
            }

        @Throws(java.lang.IllegalArgumentException::class)
        fun of(ext: String) =
            values().singleOrNull {
                it.name == ext.toUpperCase() || it.ext.contains(ext.toLowerCase())
            } ?: throw IllegalArgumentException("Media type $ext is not supported")
    }

    override fun toString(): String {
        return norm
    }
}
