package org.bibletranslationtools.common.usecases

import io.reactivex.Completable
import org.bibletranslationtools.common.extensions.MediaExtensions
import org.bibletranslationtools.common.validators.CueValidator
import org.bibletranslationtools.common.validators.JpgValidator
import org.bibletranslationtools.common.validators.Mp3Validator
import org.bibletranslationtools.common.validators.TrValidator
import org.bibletranslationtools.common.validators.WavValidator
import java.io.File

class ValidateFile(private val file: File) {

    fun validate(): Completable {
        return Completable.fromCallable {
            when (MediaExtensions.of(file.extension)) {
                MediaExtensions.TR -> TrValidator(file).validate()
                MediaExtensions.WAV -> WavValidator(file).validate()
                MediaExtensions.MP3 -> Mp3Validator(file).validate()
                MediaExtensions.CUE -> CueValidator(file).validate()
                MediaExtensions.JPG -> JpgValidator(file).validate()
            }
        }
    }
}
