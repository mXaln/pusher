package org.bibletranslationtools.common.validators

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.File

class Mp3ValidatorTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testGoodMp3File() {
        val file = File(javaClass.getResource("/test.mp3").file)
        val validator = Mp3Validator(file)
        validator.validate()
    }

    @Test
    fun testBadMp3File() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Not a mp3 file")

        val file = File(javaClass.getResource("/fake.mp3").file)
        val validator = Mp3Validator(file)
        validator.validate()
    }
}
