package org.bibletranslationtools.common.validators

import org.bibletranslationtools.common.audio.BttrChunk
import org.bibletranslationtools.common.audio.BttrMetadata
import org.wycliffeassociates.otter.common.audio.wav.InvalidWavFileException
import org.wycliffeassociates.otter.common.audio.wav.WavFile
import org.wycliffeassociates.otter.common.audio.wav.WavMetadata
import java.io.File
import java.util.regex.Pattern

class WavValidator(private val file: File): Validator() {

    /**
     * Validates WAV file
     * @throws InvalidWavFileException
     */
    override fun validate() {
        if (isChunkOrVerse()) {
            val bttrChunk = BttrChunk()
            val wavMetadata = WavMetadata(listOf(bttrChunk))
            WavFile(file, wavMetadata)

            if (!validateChunk(bttrChunk.metadata)) {
                throw InvalidWavFileException("Chunk has corrupt metadata")
            }
        } else {
            WavFile(file)
        }
    }

    private fun validateChunk(metadata: BttrMetadata): Boolean {
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
            .not()
    }

    private fun isChunkOrVerse(): Boolean {
        val chunkPattern = "_v[\\d]{1,3}(?:-[\\d]{1,3})?"
        val pattern = Pattern.compile(chunkPattern, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(file.nameWithoutExtension)

        return matcher.find()
    }
}
