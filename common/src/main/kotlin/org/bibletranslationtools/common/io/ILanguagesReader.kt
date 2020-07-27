package org.bibletranslationtools.common.io

import io.reactivex.Single

interface ILanguagesReader {
    fun read(): Single<List<String>>
}
