package org.bibletranslationtools.common.validators

import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File

class OratureValidator(private val file: File) {
    private val creatorName = "Orature"

    @Throws(Exception::class)
    fun validate() {
        try {
            ResourceContainer.load(file).use {
                if (it.manifest.dublinCore.creator != creatorName) {
                    throw Exception()
                }
            }
        } catch (ex: Exception) {
            throw Exception("Invalid Orature file")
        }
    }
}