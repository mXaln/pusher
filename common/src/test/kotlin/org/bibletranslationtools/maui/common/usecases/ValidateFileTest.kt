package org.bibletranslationtools.maui.common.usecases

import org.junit.Test
import org.wycliffeassociates.otter.common.audio.wav.InvalidWavFileException
import java.io.File

class ValidateFileTest {

    @Test
    fun testValidateFileSuccess() {
        val file = File(javaClass.getResource("/en_ulb_b41_mat_c01_v01_t01.wav").file)
        val result = ValidateFile(file).validate().test()

        result.assertComplete()
        result.assertNoErrors()
    }

    @Test
    fun testValidateFileFailed() {
        val file = File(javaClass.getResource("/en_ulb_b41_mat_c01_v01_t01_bad.wav").file)
        val result = ValidateFile(file).validate().test()

        result.assertError(InvalidWavFileException::class.java)
        result.assertErrorMessage("Chunk has corrupt metadata")
        result.assertNotComplete()
    }
}
