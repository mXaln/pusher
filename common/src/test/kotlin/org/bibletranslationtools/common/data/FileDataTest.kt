package org.bibletranslationtools.common.data

import org.junit.Assert
import org.junit.Test
import java.io.File

class FileDataTest {

    private val fileDataTr = FileData(File("test.tr"))
    private val fileDataMp3 = FileData(File("test.mp3"))
    private val fileDataWav = FileData(File("test.wav"))

    @Test
    fun mediaExtensionAvailable() {
        Assert.assertTrue(fileDataTr.mediaExtensionAvailable)
    }

    @Test
    fun mediaExtensionNotAvailable() {
        Assert.assertFalse(fileDataMp3.mediaExtensionAvailable)
    }

    @Test
    fun mediaQualityAvailable() {
        Assert.assertTrue(fileDataMp3.mediaQualityAvailable)
    }

    @Test
    fun mediaQualityNotAvailable() {
        Assert.assertFalse(fileDataWav.mediaQualityAvailable)
    }
}
