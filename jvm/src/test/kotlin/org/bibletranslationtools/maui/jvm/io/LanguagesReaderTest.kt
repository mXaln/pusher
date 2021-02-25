package org.bibletranslationtools.maui.jvm.io

import org.junit.Test

class LanguagesReaderTest {

    @Test
    fun testReadLanguagesFile() {

        val result = LanguagesReader().read().test()

        result.assertComplete()
        result.assertNoErrors()
    }
}
