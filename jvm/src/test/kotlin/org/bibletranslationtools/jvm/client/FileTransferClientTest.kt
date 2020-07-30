package org.bibletranslationtools.jvm.client

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import org.bibletranslationtools.common.client.IFileTransferClient
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.lang.IllegalArgumentException

class FileTransferClientTest {

    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    @Test
    fun testTransferComplete() {
        val client = mock<IFileTransferClient> {
            on { transfer() } doReturn Completable.complete()
        }

        val result = client.transfer().test()

        result.assertComplete()
        result.assertNoErrors()
    }

    @Test
    fun testTransferFailed() {
        val client = mock<IFileTransferClient> {
            on { transfer() } doReturn Completable.error(Exception("An error occurred"))
        }

        val result = client.transfer().test()

        result.assertError(Exception::class.java)
        result.assertErrorMessage("An error occurred")
        result.assertNotComplete()
    }

    @Test
    fun testCopySuccessful() {
        val source = tempFolder.newFile("en_ulb_gen_verse.tr")
        val target = File(tempFolder.root, "en/ulb/gen/CONTENTS/tr/wav/verse/en_ulb_gen_verse.tr")
        val client = FileTransferClient(source, target)

        val result = client.transfer().test()

        result.assertComplete()
        result.assertNoErrors()
        Assert.assertTrue(target.exists())
    }

    @Test
    fun testSourceIsDirectoryThrowsException() {
        val source = tempFolder.newFolder("en")
        val target = File(tempFolder.root, "en/ulb/gen/CONTENTS/tr/wav/verse/en_ulb_gen_verse.tr")
        val client = FileTransferClient(source, target)

        val result = client.transfer().test()

        result.assertNotComplete()
        result.assertError(IllegalArgumentException::class.java)
        result.assertErrorMessage("Source should not be a directory")
    }
}
