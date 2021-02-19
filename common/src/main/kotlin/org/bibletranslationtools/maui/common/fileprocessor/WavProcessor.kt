package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.validators.WavValidator
import org.wycliffeassociates.otter.common.audio.wav.InvalidWavFileException
import java.io.File
import java.util.Queue

class WavProcessor: FileProcessor {
    override fun process(
            file: File,
            fileQueue: Queue<File>,
            resultList: MutableList<FileResult>
    ) {
        val status = try {
            WavValidator(file).validate()
            FileStatus.PROCESSED
        } catch(ex: InvalidWavFileException) {
            FileStatus.REJECTED
        }

        resultList.add(FileResult(file, status))
    }
}