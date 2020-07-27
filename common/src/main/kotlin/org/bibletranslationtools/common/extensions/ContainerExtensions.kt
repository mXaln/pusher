package org.bibletranslationtools.common.extensions

enum class ContainerExtensions(val ext: String) {
    TR("tr");

    companion object {
        fun isValid(ext: String) =
            values().any {
                it.name == ext.toUpperCase() || it.ext == ext
            }
    }
}
