package org.bibletranslationtools.common.audio

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder


private const val RIFF = "RIFF"
private const val WAVE = "WAVE"
private const val FMT = "fmt "
private const val PCM: Short = 1
private const val PCM_POSITION = 20

private const val AUDIO_LENGTH_LOCATION = 40
private const val BITS_PER_SAMPLE_POSITION = 34

const val DEFAULT_SAMPLE_RATE = 44100
const val DEFAULT_CHANNELS = 1
const val DEFAULT_BITS_PER_SAMPLE = 16

const val CUE_LABEL = "cue "
const val LIST_LABEL = "LIST"
const val ADTL_LABEL = "adtl"
const val LABEL_LABEL = "labl"
const val INFO_LABEL = "INFO"
const val IART_LABEL = "IART"

const val LABEL_SIZE = 4
const val CHUNK_HEADER_SIZE = 8
const val CUE_DATA_SIZE = 24
const val WAV_HEADER_SIZE = 44

class InvalidWavFileException(message: String? = null) : java.lang.Exception(message)

class WavFileReader(private val file: File) {

    private val cueListBuilder = CueListBuilder()
    private val metadataMapper: MetadataMapper = MetadataMapper()

    private var metadata: Metadata = Metadata()
    private val cues: List<CuePoint> = mutableListOf()

    internal var totalAudioLength = 0
    internal var totalDataLength = 0

    var sampleRate: Int = DEFAULT_SAMPLE_RATE
        private set
    var channels: Int = DEFAULT_CHANNELS
        private set
    var bitsPerSample: Int = DEFAULT_BITS_PER_SAMPLE
        private set

    private fun readFile() {
        cueListBuilder.clear()

        parseHeader()
        parseMetadata()
    }

    @Throws(InvalidWavFileException::class)
    private fun parseHeader() {
        if (file.length() >= WAV_HEADER_SIZE) {
            RandomAccessFile(file, "r").use {
                val header = ByteArray(WAV_HEADER_SIZE)
                it.read(header)
                val bb = ByteBuffer.wrap(header)
                bb.order(ByteOrder.LITTLE_ENDIAN)
                val riff = bb.getText(4)
                this.totalDataLength = bb.int
                val wave = bb.getText(4)
                val fmt = bb.getText(4)
                bb.position(PCM_POSITION)
                val pcm = bb.short
                channels = bb.short.toInt()
                sampleRate = bb.int
                bb.position(BITS_PER_SAMPLE_POSITION)
                bitsPerSample = bb.short.toInt()
                // Seek to the audio length field
                bb.position(AUDIO_LENGTH_LOCATION)
                totalAudioLength = bb.int
                if (!validate(riff, wave, fmt, pcm)) {
                    throw InvalidWavFileException()
                }
            }
        } else {
            throw InvalidWavFileException()
        }
    }

    private fun parseMetadata() {
        if (totalDataLength > totalAudioLength + 36) {
            val metadataSize = totalDataLength - totalAudioLength - 36
            val bytes = ByteArray(metadataSize)
            file.inputStream().use {
                val metadataStart = WAV_HEADER_SIZE + totalAudioLength
                it.skip(metadataStart.toLong())
                it.read(bytes)
            }

            parseCueChunk(ByteBuffer.wrap(bytes))
            parseMetadataChunk(ByteBuffer.wrap(bytes))
        }
    }

    private fun parseCueChunk(chunk: ByteBuffer) {
        chunk.order(ByteOrder.LITTLE_ENDIAN)
        cueListBuilder.clear()
        while (chunk.remaining() > 8) {
            val subchunkLabel = chunk.getText(LABEL_SIZE)
            val subchunkSize = chunk.int

            if (chunk.remaining() < subchunkSize) {
                throw InvalidWavFileException(
                    "Chunk $subchunkLabel is of size: $subchunkSize but remaining chunk size is ${chunk.remaining()}"
                )
            }

            // section off a buffer from this one that can be used for parsing the nested chunk
            val buffer = chunk.slice()
            buffer.limit(buffer.position() + subchunkSize)

            when (subchunkLabel) {
                LIST_LABEL -> parseLabels(buffer)
                CUE_LABEL -> parseCues(buffer)
                else -> Unit
            }

            // move on to the next chunk
            chunk.seek(subchunkSize)
        }
        cues as MutableList
        cues.clear()
        cues.addAll(cueListBuilder.build())
    }

    private fun parseCues(chunk: ByteBuffer) {
        chunk.order(ByteOrder.LITTLE_ENDIAN)
        if (!chunk.hasRemaining()) {
            return
        }
        // read number of cues
        val numCues = chunk.int

        // each cue subchunk should be 24 bytes, plus 4 for the number of cues field
        if (chunk.remaining() != CUE_DATA_SIZE * numCues) {
            throw InvalidWavFileException()
        }

        // For each cue, read the cue Id and the cue location
        for (i in 0 until numCues) {
            val cueId = chunk.int
            val cueLoc = chunk.int

            cueListBuilder.addLocation(cueId, cueLoc)

            // Skip the next 16 bytes to the next cue point
            chunk.seek(16)
        }
    }

    private fun parseLabels(chunk: ByteBuffer) {
        chunk.order(ByteOrder.LITTLE_ENDIAN)

        // Skip List Chunks that are not subtype "adtl"
        if (chunk.remaining() < 4 || ADTL_LABEL != chunk.getText(4)) {
            return
        }

        while (chunk.remaining() > CHUNK_HEADER_SIZE) {
            val subchunk = chunk.getText(LABEL_SIZE)
            val subchunkSize = chunk.int
            when (subchunk) {
                LABEL_LABEL -> {
                    val id = chunk.int
                    val labelBytes = ByteArray(subchunkSize - 4)
                    chunk.get(labelBytes)
                    // trim necessary to strip trailing 0's used to pad to double word align
                    val label = String(labelBytes, Charsets.US_ASCII).trim { it.toByte() == 0.toByte() }
                    val labelNum = Utils.toNumeric(label)
                    if(labelNum == "") throw InvalidWavFileException()
                    cueListBuilder.addLabel(id, labelNum)
                }
                else -> {
                    chunk.seek(subchunkSize)
                }
            }
        }
    }

    private fun parseMetadataChunk(chunk: ByteBuffer) {
        chunk.order(ByteOrder.LITTLE_ENDIAN)
        while (chunk.remaining() > 8) {
            val subchunkLabel = chunk.getText(LABEL_SIZE)
            val subchunkSize = chunk.int

            if (chunk.remaining() < subchunkSize) {
                throw InvalidWavFileException(
                    "Chunk $subchunkLabel is of size: $subchunkSize but remaining chunk size is ${chunk.remaining()}"
                )
            }

            // section off a buffer from this one that can be used for parsing the nested chunk
            val buffer = chunk.slice()
            buffer.limit(buffer.position() + subchunkSize)

            when (subchunkLabel) {
                LIST_LABEL -> parseMetadata(buffer)
                else -> Unit
            }

            // move on to the next chunk
            chunk.seek(subchunkSize)
        }
    }

    private fun parseMetadata(chunk: ByteBuffer) {
        chunk.order(ByteOrder.LITTLE_ENDIAN)

        // Skip List Chunks that are not subtype "INFO"
        if (chunk.remaining() < 4 || INFO_LABEL != chunk.getText(4)) {
            return
        }

        while (chunk.remaining() > CHUNK_HEADER_SIZE) {
            val subchunk = chunk.getText(LABEL_SIZE)
            val subchunkSize = chunk.int
            when (subchunk) {
                IART_LABEL -> {
                    val labelBytes = ByteArray(subchunkSize)
                    chunk.get(labelBytes)
                    val meta = String(labelBytes, Charsets.US_ASCII)
                    parseJson(meta)
                }
                else -> {
                    chunk.seek(subchunkSize)
                }
            }
        }
    }

    private fun parseJson(json: String) {
        metadata = try {
            metadataMapper.fromJSON(json)
        } catch (e: Exception) {
            Metadata()
        }
    }

    private fun validate(
        riff: String,
        wave: String,
        fmt: String,
        pcm: Short
    ): Boolean {
        return booleanArrayOf(
            riff == RIFF,
            wave == WAVE,
            fmt == FMT,
            pcm == PCM
        ).all { true }
    }

    fun readMetadata(): Metadata {
        readFile()
        return metadata
    }
}

private class CueListBuilder {

    private data class TempCue(var location: Int?, var label: String?)

    private val map = mutableMapOf<Int, TempCue>()

    fun addLocation(id: Int, location: Int?) {
        map[id]?.let {
            it.location = location
        } ?: run {
            map.put(id, TempCue(location, null))
        }
    }

    fun addLabel(id: Int, label: String) {
        map[id]?.let {
            it.label = label
        } ?: run {
            map.put(id, TempCue(null, label))
        }
    }

    fun build(): MutableList<CuePoint> {
        val cues = mutableListOf<CuePoint>()
        for (cue in map.values) {
            cue.location?.let { loc ->
                cue.label?.let { label ->
                    cues.add(CuePoint(loc, label))
                }
            }
        }
        return cues
    }

    fun clear() {
        map.clear()
    }
}