package org.bibletranslationtools.maui.common.validators

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.File
import java.lang.IllegalArgumentException

class JpgValidatorTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testGoodJpgFile() {
        val file = File(javaClass.getResource("/test.jpg").file)
        val validator = JpgValidator(file)
        validator.validate()
    }

    @Test
    fun testBadJpgFile() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Not a jpg file")

        val file = File(javaClass.getResource("/fake.jpg").file)
        val validator = JpgValidator(file)
        validator.validate()
    }
}
