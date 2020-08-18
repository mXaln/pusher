package org.bibletranslationtools.common.validators

import org.apache.tika.Tika
import java.io.File

class Mp3Validator(private val file: File) : Validator() {

    companion object {
        private const val MP3_MIME_TYPE = "audio/mpeg"
    }

    override fun validate() {
        val fileType = Tika().detect(file)

        if (fileType != MP3_MIME_TYPE) {
            throw IllegalArgumentException("Not a mp3 file")
        }
    }
}
