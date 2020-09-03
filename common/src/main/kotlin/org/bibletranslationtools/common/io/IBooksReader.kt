package org.bibletranslationtools.common.io

import io.reactivex.Single

interface IBooksReader {
    fun read(): Single<List<String>>
}
