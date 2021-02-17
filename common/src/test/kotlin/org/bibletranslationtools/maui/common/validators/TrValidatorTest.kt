package org.bibletranslationtools.maui.common.validators

import org.junit.Test
import java.io.File
import java.lang.Exception

class TrValidatorTest {

    @Test
    fun testTrFileExtracts() {
        val file = File(javaClass.getResource("/en_ulb_mat_verse.tr").file)
        val validator = TrValidator(file)
        validator.validate()
    }

    @Test(expected = Exception::class)
    fun testTrFileExtractFails() {
        val file = File(javaClass.getResource("/fake.tr").file)
        val validator = TrValidator(file)
        validator.validate()
    }
}
