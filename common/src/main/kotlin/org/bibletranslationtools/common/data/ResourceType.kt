package org.bibletranslationtools.common.data

import java.lang.IllegalArgumentException

enum class ResourceType(val slug: String) {
    ULB("ulb");

    companion object {
        fun of(slug: String) =
            values().singleOrNull {
                it.name == slug.toUpperCase() || it.slug == slug
            } ?: throw IllegalArgumentException("There is no resource type $slug")
    }
}
