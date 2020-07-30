package org.bibletranslationtools.jvm.client

import io.reactivex.Completable
import org.bibletranslationtools.common.client.IFileTransferClient
import java.io.File

class FileTransferClient(
    private val source: File,
    private val target: File
): IFileTransferClient {

    override fun transfer(): Completable {
        return Completable.fromCallable {
            source.copyTo(target, true)
        }
    }
}
