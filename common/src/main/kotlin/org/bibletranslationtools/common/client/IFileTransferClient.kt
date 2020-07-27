package org.bibletranslationtools.common.client

import io.reactivex.Completable

interface IFileTransferClient {
    fun transfer(): Completable
}
