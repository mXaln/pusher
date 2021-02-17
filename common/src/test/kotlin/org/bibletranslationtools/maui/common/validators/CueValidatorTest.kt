package org.bibletranslationtools.maui.common.validators

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.File

class CueValidatorTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testGoodPlainTextFile() {
        val file = File(javaClass.getResource("/test.cue").file)
        val validator = CueValidator(file)
        validator.validate()
    }

    @Test
    fun testBadPlainTextFile() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Not a plain text file")

        val file = File(javaClass.getResource("/fake.cue").file)
        val validator = CueValidator(file)
        validator.validate()
    }
}
