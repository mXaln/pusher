package org.bibletranslationtools.common.validators

import org.bibletranslationtools.common.audio.BttrChunk
import org.bibletranslationtools.common.audio.BttrMetadata
import org.wycliffeassociates.otter.common.audio.wav.InvalidWavFileException
import org.wycliffeassociates.otter.common.audio.wav.WavFile
import org.wycliffeassociates.otter.common.audio.wav.WavMetadata
import java.io.File
import java.util.regex.Pattern

class WavValidator(private val file: File): Validator() {

    companion object {
        private const val LANGUAGE = "([a-zA-Z]{2,3}[-a-zA-Z]*?)"
        private const val ANTHOLOGY = "(?:_(?:nt|ot))?"
        private const val RESOURCE_TYPE = "(?:_([a-zA-Z]{3}))"
        private const val BOOK_NUMBER = "(?:_b([\\d]{2}))?"
        private const val BOOK = "(?:_([1-3]{0,1}[a-zA-Z]{2,3}))"
        private const val CHAPTER = "(?:_c([\\d]{1,3}))"
        private const val META = "(?:_meta)?"
        private const val VERSE = "(?:_v([\\d]{1,3})(?:-([\\d]{1,3}))?)"
        private const val TAKE = "(?:_t([\\d]{1,2}))?"
        private const val CHAPTER_FILE_PATTERN = LANGUAGE + ANTHOLOGY + RESOURCE_TYPE +
                BOOK_NUMBER + BOOK + CHAPTER + META + TAKE
        private const val CHUNK_VERSE_FILE_PATTERN = LANGUAGE + ANTHOLOGY + RESOURCE_TYPE +
                BOOK_NUMBER + BOOK + CHAPTER + VERSE + TAKE
    }

    /**
     * Validates WAV file
     * @throws InvalidWavFileException
     */
    override fun validate() {
        when {
            isChunkOrVerse() -> {
                val bttrChunk = BttrChunk()
                val wavMetadata = WavMetadata(listOf(bttrChunk))
                WavFile(file, wavMetadata)

                if (!validateBttrMetadata(bttrChunk.metadata)) {
                    throw InvalidWavFileException("Chunk has corrupt metadata")
                }

                if (!validateChunkVerseFileName()) {
                    throw InvalidWavFileException("Chunk/verse filename is incorrect")
                }
            }
            isChapter() -> {
                if (!validateChapterFileName()) {
                    throw InvalidWavFileException("Chapter filename is incorrect")
                }
            }
            else -> WavFile(file)
        }
    }

    private fun validateBttrMetadata(metadata: BttrMetadata): Boolean {
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

    private fun validateChapterFileName(): Boolean {
        val pattern = Pattern.compile(CHAPTER_FILE_PATTERN, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(file.nameWithoutExtension)

        return matcher.find()
    }

    private fun validateChunkVerseFileName(): Boolean {
        val pattern = Pattern.compile(CHUNK_VERSE_FILE_PATTERN, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(file.nameWithoutExtension)

        return matcher.find()
    }

    private fun isChunkOrVerse(): Boolean {
        val chunkPattern = "_v[\\d]{1,3}(?:-[\\d]{1,3})?"
        val pattern = Pattern.compile(chunkPattern, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(file.nameWithoutExtension)

        return matcher.find()
    }

    private fun isChapter(): Boolean {
        val chapterPattern = "_c([\\d]{1,3})"
        val pattern = Pattern.compile(chapterPattern, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(file.nameWithoutExtension)

        return matcher.find()
    }
}
