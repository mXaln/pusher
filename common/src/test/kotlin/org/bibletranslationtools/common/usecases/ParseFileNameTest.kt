package org.bibletranslationtools.common.usecases

import io.reactivex.subscribers.TestSubscriber
import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException

class ParseFileNameTest {

    private val file1 = File("en_ulb_b01_gen_c01_v01_t01.wav")
    private val file2 = File("EN_ULB_B01_GEN_C01_V01_T01.wav")
    private val file3 = File("test.wav")
    private val file4 = File("en_ulb_gen_c02_chunk.tr")
    private val file5 = File("en_ulb_gen_low_verse.mp3")
    private val file6 = File("en_udb_gen.tr")

    private val fileData1 = FileData(
        file1,
        "en",
        ResourceType.ULB,
        "gen",
        1,
        null,
        null,
        Grouping.VERSE
    )

    private val fileData2 = FileData(
        file2,
        "en",
        ResourceType.ULB,
        "gen",
        1,
        null,
        null,
        Grouping.VERSE
    )

    private val fileData3 = FileData(file3)

    private val fileData4 = FileData(
        file4,
        "en",
        ResourceType.ULB,
        "gen",
        2,
        null,
        null,
        Grouping.CHUNK
    )

    private val fileData5 = FileData(
        file5,
        "en",
        ResourceType.ULB,
        "gen",
        null,
        null,
        MediaQuality.LOW,
        Grouping.VERSE
    )

    private val parser1 = ParseFileName(file1)
    private val parser2 = ParseFileName(file2)
    private val parser3 = ParseFileName(file3)
    private val parser4 = ParseFileName(file4)
    private val parser5 = ParseFileName(file5)
    private val parser6 = ParseFileName(file6)

    private val subscriber = TestSubscriber<FileData>()

    @Test
    fun parseFileNameWithValidInfo() {
        val result = parser1.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue(fileData1)
    }

    @Test
    fun parseFileNameUpperCaseWithValidInfo() {
        val result = parser2.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue(fileData2)
    }

    @Test
    fun parseFileNameWithInvalidInfo() {
        val result = parser3.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue(fileData3)
    }

    @Test
    fun fileDataHasLanguage() {
        val result = parser1.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.language == fileData1.language
        }
    }

    @Test
    fun fileDataLanguageNullWithInvalidFileName() {
        val result = parser3.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.language == fileData3.language
        }
    }

    @Test
    fun fileDataHasGrouping() {
        val result = parser4.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.grouping == fileData4.grouping
        }
    }

    @Test
    fun fileDataHasMediaQuality() {
        val result = parser5.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.mediaQuality == fileData5.mediaQuality
        }
    }

    @Test
    fun unsupportedResourceTypeThrowsException() {
        val result = parser6.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertError(IllegalArgumentException::class.java)
        subscriber.assertNotComplete()
    }
}
