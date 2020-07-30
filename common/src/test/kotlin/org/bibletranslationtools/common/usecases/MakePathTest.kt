package org.bibletranslationtools.common.usecases

import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.junit.Test
import java.io.File

class MakePathTest {

    @Test
    fun testCompressedMedia() {
        val expected = "en/ulb/gen/1/CONTENTS/mp3/low/verse/en_ulb_gen_c01_v03_t01.mp3"
        val fileData = FileData(
            File("en_ulb_gen_c01_v03_t01.mp3"),
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            MediaQuality.LOW,
            Grouping.VERSE
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)

    }

    @Test
    fun testUncompressedMedia() {
        val expected = "en/ulb/gen/1/CONTENTS/wav/chunk/en_ulb_gen_c01_v03-05_t01.wav"
        val fileData = FileData(
            File("en_ulb_gen_c01_v03-05_t01.wav"),
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            null,
            Grouping.CHUNK
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)

    }

    @Test
    fun testContainerWithCompressedMedia() {
        val expected = "en/ulb/gen/CONTENTS/tr/mp3/hi/verse/en_ulb_gen_verse.tr"
        val fileData = FileData(
            File("en_ulb_gen_verse.tr"),
            "en",
            ResourceType.ULB,
            "gen",
            null,
            MediaExtension.MP3,
            MediaQuality.HI,
            Grouping.VERSE
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
    }

    @Test
    fun testJpegNormalized() {
        val expected = "en/ulb/gen/CONTENTS/jpg/low/book/en_ulb_gen.jpg"
        val fileData = FileData(
            File("en_ulb_gen.jpeg"),
            "en",
            ResourceType.ULB,
            "gen",
            null,
            null,
            MediaQuality.LOW,
            Grouping.BOOK
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
    }

    @Test
    fun testWrongFileDataThrowsException() {
        val fileData = FileData(
            File("test.mp3")
        )

        val result = MakePath(fileData).build().test()

        result.assertError(IllegalArgumentException::class.java)
        result.assertNotComplete()
    }

    @Test
    fun testNonContainerWithMediaExtensionThrowsException() {
        val fileData = FileData(
            File("en_ulb_gen.mp3"),
            "en",
            ResourceType.ULB,
            "gen",
            null,
            MediaExtension.MP3,
            MediaQuality.LOW,
            Grouping.CHAPTER
        )

        val result = MakePath(fileData).build().test()

        result.assertError(IllegalArgumentException::class.java)
        result.assertNotComplete()
    }

    @Test
    fun testCompressedMediaWithoutQualityThrowsException() {
        val fileData = FileData(
            File("en_ulb_gen.mp3"),
            "en",
            ResourceType.ULB,
            "gen",
            null,
            null,
            null,
            Grouping.CHAPTER
        )

        val result = MakePath(fileData).build().test()

        result.assertError(IllegalArgumentException::class.java)
        result.assertNotComplete()
    }
}
