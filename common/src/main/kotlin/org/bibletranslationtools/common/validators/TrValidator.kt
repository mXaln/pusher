package org.bibletranslationtools.common.validators

import org.wycliffeassociates.io.ArchiveOfHolding
import org.wycliffeassociates.io.LanguageLevel
import java.io.File
import java.nio.file.Files

class TrValidator(private val file: File) : IValidator {

    /**
     * Validates TR file integrity by trying to extract it
     * @throws Exception Can throw an exception if extraction fails
     */
    override fun validate() {
        file.inputStream().use { fis ->
            fis.buffered().use { bis ->
                val ll = LanguageLevel()
                val aoh = ArchiveOfHolding(bis, ll)
                val out = Files.createTempDirectory("temp").toFile()
                aoh.extractArchive(file, out)
                out.deleteOnExit()
            }
        }
    }
}
