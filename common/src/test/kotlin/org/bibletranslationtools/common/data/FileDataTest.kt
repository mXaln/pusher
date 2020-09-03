package org.bibletranslationtools.common.data

import org.junit.Assert
import org.junit.Test
import java.io.File

class FileDataTest {

    private val fileDataTr = FileData(File("test.tr"))
    private val fileDataMp3 = FileData(File("test.mp3"))
    private val fileDataWav = FileData(File("test.wav"))
    private val fileDataTrCompressed = FileData(
        file = File("test.tr"),
        mediaExtension = MediaExtension.MP3
    )

    @Test
    fun testMediaIsContainer() {
        Assert.assertTrue(fileDataTr.isContainer)
    }

    @Test
    fun testMediaIsCompressed() {
        Assert.assertTrue(fileDataMp3.isCompressed)
    }

    @Test
    fun testMediaIsNotCompressed() {
        Assert.assertTrue(!fileDataWav.isCompressed)
    }

    @Test
    fun testMediaIsContainerAndCompressed() {
        Assert.assertTrue(fileDataTrCompressed.isContainerAndCompressed)
    }
}
