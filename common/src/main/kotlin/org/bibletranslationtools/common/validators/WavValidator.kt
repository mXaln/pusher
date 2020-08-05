package org.bibletranslationtools.common.validators

import org.bibletranslationtools.common.audio.InvalidWavFileException
import org.bibletranslationtools.common.audio.BttrMetadata
import org.bibletranslationtools.common.audio.WavFileReader
import java.io.File

class WavValidator(private val file: File): Validator() {

    /**
     * Validates WAV file and its metadata
     * @throws InvalidWavFileException
     */
    override fun validate() {
        val wavReader = WavFileReader(file)
        val metadata = wavReader.readMetadata()

        if (hasBadMetadata(metadata)) {
            throw InvalidWavFileException("Bad metadata")
        }
    }

    private fun hasBadMetadata(metadata: BttrMetadata): Boolean {
        return metadata.language.isBlank()
            .or(metadata.anthology.isBlank())
            .or(metadata.version.isBlank())
            .or(metadata.bookNumber.isBlank())
            .or(metadata.slug.isBlank())
            .or(metadata.mode.isBlank())
            .or(metadata.chapter.isBlank())
            .or(metadata.startv.isBlank())
            .or(metadata.endv.isBlank())
            .or(metadata.markers.isEmpty())
    }
}
