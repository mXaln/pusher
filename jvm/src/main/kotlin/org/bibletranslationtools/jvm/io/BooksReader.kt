package org.bibletranslationtools.jvm.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.reactivex.Single
import org.bibletranslationtools.common.io.IBooksReader
import java.io.File

class BooksReader : IBooksReader {

    companion object {
        const val CATALOG_SLUG_ID = "slug"
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class BookSchema(
        @JsonProperty(CATALOG_SLUG_ID) val slug: String
    )

    override fun read(): Single<List<String>> {
        return Single.fromCallable {
            parseBooks()
        }
    }

    private fun parseBooks(): List<String> {
        val booksFile = File(javaClass.getResource("/book_catalog.json").file)

        val booksList: List<BookSchema> = jacksonObjectMapper().readValue(booksFile)

        return booksList.map {
            it.slug
        }
    }
}
