package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileData
import org.bibletranslationtools.maui.common.data.Grouping
import org.bibletranslationtools.maui.common.data.MediaQuality
import org.bibletranslationtools.maui.common.data.ResourceType
import org.junit.Test
import java.io.File

class ParseFileNameTest {

    @Test
    fun parseFileNameWithValidInfo() {
        val file = File("en_ot_ulb_b01_gen_c01_v01_t01.wav")
        val expected = FileData(
            file,
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            null,
            null
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
    }

    @Test
    fun parseFileNameWithInvalidInfo() {
        val file = File("test.wav")
        val expected = FileData(file)
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
    }

    @Test
    fun parseFileNameUpperCaseWithValidInfo() {
        val file = File("EN_ULB_B01_GEN_C01_V01_T01.wav")
        val expected = FileData(
            file,
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            null,
            null
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
    }

    @Test
    fun fileDataHasLanguage() {
        val file = File("en_ot_ulb_b01_gen_c01_v01_t01.wav")
        val expected = FileData(
            file,
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            null,
            Grouping.VERSE
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue {
            it.language == expected.language
        }
    }

    @Test
    fun fileDataLanguageNullWithInvalidFileName() {
        val file = File("test.wav")
        val expected = FileData(file)
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue {
            it.language == expected.language
        }
    }

    @Test
    fun fileDataHasGrouping() {
        val file = File("en_ulb_gen_c02_chunk.tr")
        val expected = FileData(
            file,
            "en",
            ResourceType.ULB,
            "gen",
            2,
            null,
            null,
            Grouping.CHUNK
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue {
            it.grouping == expected.grouping
        }
    }

    @Test
    fun fileDataHasMediaQuality() {
        val file = File("en_ulb_gen_low_verse.mp3")
        val expected = FileData(
            file,
            "en",
            ResourceType.ULB,
            "gen",
            null,
            null,
            MediaQuality.LOW,
            Grouping.VERSE
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue {
            it.mediaQuality == expected.mediaQuality
        }
    }

    @Test
    fun unsupportedResourceTypeThrowsException() {
        val file = File("en_udb_gen.tr")
        val result = ParseFileName(file).parse().test()

        result.assertError(IllegalArgumentException::class.java)
        result.assertErrorMessage("Resource type udb is not supported")
        result.assertNotComplete()
    }

    @Test
    fun parseFileNameLanguageResourceOnly() {
        val file = File("en_ulb.wav")
        val fileData = FileData(
            file,
            "en",
            ResourceType.ULB
        )
        val result = ParseFileName(file).parse().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(fileData)
    }
}
