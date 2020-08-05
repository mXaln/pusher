package org.bibletranslationtools.jvm.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import io.reactivex.Single
import org.bibletranslationtools.common.io.ILanguagesReader
import java.io.File


class LanguagesReader : ILanguagesReader {

    companion object {
        const val PORT_LANGUAGE_CODE_ID = "IETF Tag"
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class PortGatewayLanguage(
        @JsonProperty(PORT_LANGUAGE_CODE_ID)
        val code: String
    )

    override fun read(): Single<List<String>> {
        return Single.fromCallable {
            parseLanguages()
        }
    }

    private fun parseLanguages(): List<String> {
        val languagesFile = File(javaClass.getResource("/port_gateway_languages.csv").file)

        val mapper = CsvMapper()
        val schema = CsvSchema.emptySchema().withHeader()

        val languagesIterator: MappingIterator<PortGatewayLanguage> = mapper.readerFor(
            PortGatewayLanguage::class.java
        )
            .with(schema)
            .readValues(languagesFile)

        val languages = mutableListOf<String>()
        languagesIterator.forEach {
            languages.add(it.code)
        }

        languages.sort()

        return languages
    }
}
