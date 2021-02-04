package org.bibletranslationtools.common.usecases

import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File

class ValidateOratureFile(private val file: File) {
    private val creatorName = "Orature"

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
