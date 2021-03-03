package org.bibletranslationtools.maui.common.usecases

import org.bibletranslationtools.maui.common.data.FileData
import org.bibletranslationtools.maui.common.data.Grouping
import org.bibletranslationtools.maui.common.data.MediaExtension
import org.bibletranslationtools.maui.common.data.MediaQuality
import org.bibletranslationtools.maui.common.data.ResourceType
import org.junit.Test
import java.io.File

class MakePathTest {

    @Test
    fun testCompressedMedia() {
        val expected = "en/ulb/gen/1/CONTENTS/mp3/low/verse/en_ulb_gen_c01_v03.mp3"
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
        val expected = "en/ulb/gen/1/CONTENTS/wav/chunk/en_ulb_gen_c01_v03-05.wav"
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
    fun testChapterFile() {
        val expected = "en/ulb/gen/1/CONTENTS/mp3/low/chapter/en_ulb_gen_c1.mp3"
        val fileData = FileData(
            File("en_ulb_gen_c01.mp3"),
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            MediaQuality.LOW,
            Grouping.CHAPTER
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)

    }

    @Test
    fun testContainerWithCompressedMedia() {
        val expected = "en/ulb/gen/CONTENTS/tr/mp3/hi/verse/en_ulb_gen.tr"
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
    fun testNormalizeFilename() {
        val expected = "en/ulb/gen/1/CONTENTS/mp3/hi/chapter/en_ulb_gen_c1.mp3"
        val fileData = FileData(
            File("test.mp3"),
            "en",
            ResourceType.ULB,
            "gen",
            1,
            null,
            MediaQuality.HI,
            Grouping.CHAPTER
        )

        val result = MakePath(fileData).build().test()

        result.assertComplete()
        result.assertNoErrors()
        result.assertValue(expected)
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
        result.assertErrorMessage("Media extension cannot be applied to non-container media")
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
        result.assertErrorMessage("Media quality needs to be specified for compressed media")
        result.assertNotComplete()
    }
}
