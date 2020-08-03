package org.bibletranslationtools.common.extensions

interface SupportedExtensions {
    fun isSupported(ext: String): Boolean
}
