package org.bibletranslationtools.common.usecases

import io.reactivex.Completable
import org.bibletranslationtools.common.client.IFileTransferClient

class TransferFile(private val client: IFileTransferClient) {

    fun transfer(): Completable {
        return client.transfer()
    }
}
