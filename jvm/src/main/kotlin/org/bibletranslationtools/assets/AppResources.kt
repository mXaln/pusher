package org.bibletranslationtools.assets

object AppResources {
    fun load(path: String): String {
        return javaClass.getResource(path).toExternalForm()
    }
}
