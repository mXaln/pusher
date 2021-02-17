package org.bibletranslationtools.maui.common.client

import io.reactivex.Completable

interface IFileTransferClient {
    fun transfer(): Completable
}
