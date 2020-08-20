package org.bibletranslationtools.common.validators

import org.apache.tika.Tika
import java.io.File

class JpgValidator(private val file: File) : IValidator {
    companion object {
        private const val JPG_MIME_TYPE = "image/jpeg"
    }

    override fun validate() {
        val fileType = Tika().detect(file)

        if (fileType != JPG_MIME_TYPE) {
            throw IllegalArgumentException("Not a jpg file")
        }
    }
}
