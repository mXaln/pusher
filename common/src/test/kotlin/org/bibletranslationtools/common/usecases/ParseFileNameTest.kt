package org.bibletranslationtools.common.usecases

import io.reactivex.subscribers.TestSubscriber
import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.junit.Test
import java.io.File

class ParseFileNameTest {

    private val file1 = File("en_ulb_b01_gen_c01_v01_t01.wav")
    private val file2 = File("test.wav")
    private val file3 = File("en_ulb_gen_c02_chunk.tr")
    private val file4 = File("en_ulb_gen_low_verse.mp3")

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

    private val fileData2 = FileData(file2)

    private val fileData3 = FileData(
        file3,
        "en",
        ResourceType.ULB,
        "gen",
        2,
        null,
        null,
        Grouping.CHUNK
    )

    private val fileData4 = FileData(
        file4,
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
    fun parseFileNameWithInvalidInfo() {
        val result = parser2.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue(fileData2)
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
        val result = parser2.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.language == fileData2.language
        }
    }

    @Test
    fun fileDataHasGrouping() {
        val result = parser3.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.grouping == fileData3.grouping
        }
    }

    @Test
    fun fileDataHasMediaQuality() {
        val result = parser4.parse().toFlowable()
        result.subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue {
            it.mediaQuality == fileData4.mediaQuality
        }
    }
}
