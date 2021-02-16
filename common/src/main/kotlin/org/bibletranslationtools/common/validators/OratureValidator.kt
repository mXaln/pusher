package org.bibletranslationtools.common.validators

import org.slf4j.LoggerFactory
import org.wycliffeassociates.resourcecontainer.ResourceContainer
import java.io.File

class OratureValidator(private val file: File) {
    private val creatorName = "Orature"
    private val logger = LoggerFactory.getLogger(javaClass)

    fun isValid(): Boolean {
        try {
            ResourceContainer.load(file).use {
                if (it.manifest.dublinCore.creator != creatorName) {
                    return false
                }
            }
            return true
        } catch (ex: Exception) {
            logger.error("Invalid Orature file", ex)
            return false
        }
    }
}